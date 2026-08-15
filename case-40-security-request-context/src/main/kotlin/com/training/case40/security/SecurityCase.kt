package com.training.case40.security

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
import org.springframework.security.oauth2.jwt.Jwt
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
object CorrelationContext { fun set(id: String): Unit = TODO(); fun get(): String = TODO(); fun clear(): Unit = TODO() }

@Component
class CorrelationInterceptor : HandlerInterceptor {
 // TODO 2: Gjenbruk gyldig header eller generer UUID, sett response-header og rydd alltid.
 override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean = true
 override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) { /* TODO: CorrelationContext.clear() */ }
}

@Configuration class WebConfig(private val interceptor: CorrelationInterceptor) : WebMvcConfigurer { override fun addInterceptors(registry: InterceptorRegistry) { registry.addInterceptor(interceptor) } }

@Component
class CurrentUserMapper {
 // TODO 3: Map subject og claimet roles til intern modell; avvis ukjente roller bevisst.
 fun from(jwt: Jwt): CurrentUser = TODO("Map JWT til CurrentUser")
}

// TODO 7: USER kan lese egen; MANAGER kan lese alle.
fun CurrentUser.canRead(case: CaseEntity): Boolean = TODO("Implementer eierskaps-/rollerregel")

@Service
class CaseService(private val cases: CaseRepository, private val audits: AuditRepository) {
 @Transactional(readOnly=true) fun get(id: Long, currentUser: CurrentUser): CaseResponse = TODO("Hent, autoriser og map")
 // TODO 5 og 8: Aktiver @PreAuthorize og gjør status+audit atomisk.
 @Transactional fun approve(id: Long, currentUser: CurrentUser): CaseResponse = TODO("Godkjenn og lagre audit")
}

@RestController @RequestMapping("/api/cases")
class CaseController(private val service: CaseService, private val mapper: CurrentUserMapper) {
 @GetMapping("/{id}") fun get(@PathVariable id: Long, @AuthenticationPrincipal jwt: Jwt) = service.get(id, mapper.from(jwt))
 @PostMapping("/{id}/approve") fun approve(@PathVariable id: Long, @AuthenticationPrincipal jwt: Jwt) = service.approve(id, mapper.from(jwt))
}

@Configuration @EnableMethodSecurity
class SecurityConfig {
 // TODO 4: Stateless JWT, autentiserte API-er og riktig authority/rolle-policy.
 @Bean fun filterChain(http: HttpSecurity): SecurityFilterChain { http.csrf { it.disable() }.authorizeHttpRequests { it.anyRequest().permitAll() }; return http.build() }
}

@RestControllerAdvice
class SecurityCaseErrors {
 @ExceptionHandler(CaseNotFound::class) @ResponseStatus(HttpStatus.NOT_FOUND) fun notFound(ex: CaseNotFound)=mapOf("error" to ex.message)
 @ExceptionHandler(AccessDenied::class) @ResponseStatus(HttpStatus.FORBIDDEN) fun denied(ex: AccessDenied)=mapOf("error" to ex.message)
}
