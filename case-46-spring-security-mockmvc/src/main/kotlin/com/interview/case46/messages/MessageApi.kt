package com.interview.case46.messages

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class MessageApplication

fun main(args: Array<String>) {
    runApplication<MessageApplication>(*args)
}

data class CreateMessageRequest(val text: String)
data class MessageResponse(val id: Long, val text: String)

@Service
class MessageService {
    fun list(): List<MessageResponse> = listOf(MessageResponse(1, "Velkommen"))
    fun create(request: CreateMessageRequest): MessageResponse = MessageResponse(2, request.text)
}

@RestController
@RequestMapping("/api/messages")
class MessageController(private val service: MessageService) {
    @GetMapping fun list() = service.list()
    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: CreateMessageRequest) = service.create(request)
}

@Configuration
class MessageSecurityConfig {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.POST, "/api/messages").hasAuthority("SCOPE_messages:write")
                it.requestMatchers(HttpMethod.GET, "/api/messages").hasAuthority("SCOPE_messages:read")
                it.anyRequest().authenticated()
            }
            .oauth2ResourceServer { it.jwt {} }
        return http.build()
    }
}
