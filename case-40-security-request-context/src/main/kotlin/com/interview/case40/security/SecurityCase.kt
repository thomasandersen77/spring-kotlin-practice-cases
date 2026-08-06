package com.interview.case40.security

import jakarta.persistence.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.web.SecurityFilterChain
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.Instant
import java.util.UUID

@SpringBootApplication class SecurityCaseApplication
fun main(args: Array<String>) { runApplication<SecurityCaseApplication>(*args) }

enum class Role { USER, MANAGER }
data class CurrentUser(val subject: String, val roles: Set<Role>)
enum class CaseStatus { OPEN, APPROVED }
data class CaseResponse(val id: Long, val ownerSubject: String, val status: CaseStatus)

@Entity @Table(name="approval_cases")
class CaseEntity(@Id @GeneratedValue(strategy=GenerationType.IDENTITY) var id: Long?=null, @Column(nullable=false) var ownerSubject: String="", @Enumerated(EnumType.STRING) @Column(nullable=false) var status: CaseStatus=CaseStatus.OPEN)
@Entity @Table(name="approval_audits")
class AuditEntity(@Id @GeneratedValue(strategy=GenerationType.IDENTITY) var id: Long?=null, @Column(nullable=false) var caseId: Long=0, @Column(nullable=false) var actorSubject: String="", @Column(nullable=false) var correlationId: String="", @Column(nullable=false) var createdAt: Instant=Instant.EPOCH)
interface CaseRepository : JpaRepository<CaseEntity, Long>
interface AuditRepository : JpaRepository<AuditEntity, Long>

class AccessDenied : RuntimeException("Ikke tilgang")
class CaseNotFound(id: Long) : RuntimeException("Sak $id finnes ikke")

// TODO 1: Implementer liten correlation-holder med ThreadLocal og remove().
object CorrelationContext {
    private val holder = ThreadLocal<String>()
    fun set(id: String) { require(id.isNotBlank()) { "Correlation ID kan ikke være blank" }; holder.set(id) }
    fun get(): String = checkNotNull(holder.get()) { "Correlation ID mangler" }
    fun clear() = holder.remove()
}

@Component
class CorrelationInterceptor : HandlerInterceptor {
    // TODO 2: Gjenbruk gyldig header eller generer UUID, sett response-header og rydd alltid.
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val id = request.getHeader("X-Correlation-ID")?.trim()?.takeIf { it.isNotEmpty() } ?: UUID.randomUUID().toString()
        CorrelationContext.set(id)
        response.setHeader("X-Correlation-ID", id)
        return true
    }
    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) = CorrelationContext.clear()
}

@Configuration class WebConfig(private val interceptor: CorrelationInterceptor) : WebMvcConfigurer { override fun addInterceptors(registry: InterceptorRegistry) { registry.addInterceptor(interceptor) } }

@Component
class CurrentUserMapper {
    // TODO 3: Map subject og claimet roles til intern modell; avvis ukjente roller bevisst.
    fun from(jwt: Jwt): CurrentUser {
        val subject = jwt.subject?.takeIf { it.isNotBlank() } ?: throw IllegalArgumentException("JWT subject mangler")
        val roles = jwt.getClaimAsStringList("roles").orEmpty().map {
            try { Role.valueOf(it.uppercase()) }
            catch (_: IllegalArgumentException) { throw IllegalArgumentException("Ukjent rolle: $it") }
        }.toSet()
        return CurrentUser(subject, roles)
    }
}

// TODO 7: USER kan lese egen; MANAGER kan lese alle.
fun CurrentUser.canRead(case: CaseEntity): Boolean = Role.MANAGER in roles ||
    (Role.USER in roles && subject == case.ownerSubject)

@Service
class CaseService(private val cases: CaseRepository, private val audits: AuditRepository) {
    @Transactional(readOnly=true) fun get(id: Long, currentUser: CurrentUser): CaseResponse {
        val entity = cases.findById(id).orElseThrow { CaseNotFound(id) }
        if (!currentUser.canRead(entity)) throw AccessDenied()
        return entity.toResponse()
    }
    // TODO 5 og 8: Aktiver @PreAuthorize og gjør status+audit atomisk.
    @PreAuthorize("hasRole('MANAGER')")
    @Transactional fun approve(id: Long, currentUser: CurrentUser): CaseResponse {
        if (Role.MANAGER !in currentUser.roles) throw AccessDenied()
        val entity = cases.findById(id).orElseThrow { CaseNotFound(id) }
        if (entity.status == CaseStatus.APPROVED) throw CaseAlreadyApproved(id)
        entity.status = CaseStatus.APPROVED
        val saved = cases.save(entity)
        audits.save(AuditEntity(caseId = id, actorSubject = currentUser.subject, correlationId = CorrelationContext.get(), createdAt = Instant.now()))
        return saved.toResponse()
    }
}

class CaseAlreadyApproved(id: Long) : RuntimeException("Sak $id er allerede godkjent")
private fun CaseEntity.toResponse() = CaseResponse(requireNotNull(id), ownerSubject, status)

@RestController @RequestMapping("/api/cases")
class CaseController(private val service: CaseService, private val mapper: CurrentUserMapper) {
    @GetMapping("/{id}") fun get(@PathVariable id: Long, @AuthenticationPrincipal jwt: Jwt) = service.get(id, mapper.from(jwt))
    @PostMapping("/{id}/approve") fun approve(@PathVariable id: Long, @AuthenticationPrincipal jwt: Jwt) = service.approve(id, mapper.from(jwt))
}

@Configuration @EnableMethodSecurity
class SecurityConfig {
    // TODO 4: Stateless JWT, autentiserte API-er og riktig authority/rolle-policy.
    @Bean fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { it.requestMatchers("/api/**").authenticated().anyRequest().permitAll() }
            .oauth2ResourceServer { it.jwt { jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()) } }
        return http.build()
    }

    @Bean fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val scopes = JwtGrantedAuthoritiesConverter()
        return JwtAuthenticationConverter().apply {
            setJwtGrantedAuthoritiesConverter { jwt: Jwt ->
                val result = scopes.convert(jwt)?.toMutableSet() ?: mutableSetOf<GrantedAuthority>()
                jwt.getClaimAsStringList("roles").orEmpty().map { SimpleGrantedAuthority("ROLE_${it.uppercase()}") }.forEach(result::add)
                result
            }
        }
    }
}

@RestControllerAdvice
class SecurityCaseErrors {
    @ExceptionHandler(CaseNotFound::class) @ResponseStatus(HttpStatus.NOT_FOUND) fun notFound(ex: CaseNotFound)=mapOf("error" to ex.message)
    @ExceptionHandler(AccessDenied::class) @ResponseStatus(HttpStatus.FORBIDDEN) fun denied(ex: AccessDenied)=mapOf("error" to ex.message)
    @ExceptionHandler(CaseAlreadyApproved::class) @ResponseStatus(HttpStatus.CONFLICT) fun conflict(ex: CaseAlreadyApproved)=mapOf("error" to ex.message)
}
