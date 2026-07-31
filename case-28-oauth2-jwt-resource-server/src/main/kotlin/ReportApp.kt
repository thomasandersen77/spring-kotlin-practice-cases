package com.interview.reports

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/**
 * OAUTH2/JWT RESOURCE SERVER
 *
 * SecurityConfig er med vilje ufullstendig — alt slipper gjennom.
 * Se README for TODO-er: JWT resource server, scopes og rollemapping fra custom claim.
 */

@SpringBootApplication
class ReportApp

fun main(args: Array<String>) {
    runApplication<ReportApp>(*args)
}

data class Report(val id: String, val title: String)

@RestController
class ReportController {

    @GetMapping("/public/ping")
    fun ping(): Map<String, String> = mapOf("status" to "ok")

    // TODO: skal kreve JWT med scope reports:read
    @GetMapping("/api/reports")
    fun listReports(): List<Report> = listOf(
        Report("R1", "Kvartalsrapport"),
        Report("R2", "Kapasitetsrapport")
    )

    // TODO: skal kreve rollen ADMIN (fra custom claim "roles" i tokenet)
    @DeleteMapping("/api/reports/{id}")
    fun deleteReport(@PathVariable id: String): ResponseEntity<Void> =
        ResponseEntity.noContent().build()
}

@Configuration
class SecurityConfig {

    // TODO: Fullfør konfigurasjonen:
    //  1. permitAll på alle stier under /public
    //  2. /api/reports (GET) krever authority SCOPE_reports:read
    //  3. /api/reports/{id} (DELETE) krever rollen ADMIN
    //  4. oauth2ResourceServer { jwt { ... } } med en JwtAuthenticationConverter
    //     som mapper claimet "roles" til ROLE_-authorities
    //  5. csrf disabled + stateless sessions — og vit hvorfor
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth.anyRequest().permitAll() // <- med vilje feil: alt er åpent
            }
        return http.build()
    }
}
