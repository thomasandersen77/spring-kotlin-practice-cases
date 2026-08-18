package com.training.case44.security

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication class JwtClaimsApplication

fun main(args: Array<String>) {
	runApplication<JwtClaimsApplication>(*args)
}

class JwtAuthorityMapper : Converter<Jwt, Collection<GrantedAuthority>> {
	// TODO 1: Map permissions til SCOPE_* og roller til ROLE_*. Ignorer blanke/ukjente verdier
	// bevisst.
	override fun convert(source: Jwt): Collection<GrantedAuthority> =
		TODO("Map custom claims til authorities")
}

@Configuration
class JwtSecurityConfig {
	@Bean
	fun filterChain(http: HttpSecurity, mapper: JwtAuthorityMapper): SecurityFilterChain {
		val converter =
			JwtAuthenticationConverter().apply {
				setJwtGrantedAuthoritiesConverter(mapper)
			}
		http
			.csrf { it.disable() }
			.authorizeHttpRequests {
				it.requestMatchers(HttpMethod.GET, "/api/invoices/**")
					.hasAuthority("SCOPE_invoices:read")
				it.requestMatchers(HttpMethod.POST, "/api/invoices/**").hasRole("FINANCE_ADMIN")
				it.anyRequest().authenticated()
			}
			.oauth2ResourceServer { it.jwt { jwt -> jwt.jwtAuthenticationConverter(converter) } }
		return http.build()
	}

	@Bean fun jwtAuthorityMapper() = JwtAuthorityMapper()
}

@RestController
@RequestMapping("/api/invoices")
class InvoiceController {
	@GetMapping fun list() = listOf("invoice-1")

	@PostMapping fun create() = mapOf("id" to "invoice-2")
}
