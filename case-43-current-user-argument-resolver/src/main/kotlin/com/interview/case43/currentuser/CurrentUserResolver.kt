package com.interview.case43.currentuser

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.MethodParameter
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
class CurrentUserApplication

fun main(args: Array<String>) {
    runApplication<CurrentUserApplication>(*args)
}

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class CurrentUser

data class AuthenticatedUser(val subject: String, val tenantId: String, val roles: Set<String>)

@Component
class CurrentUserArgumentResolver : HandlerMethodArgumentResolver {
    // TODO 1: Støtt bare parametre annotert med @CurrentUser og med riktig type.
    override fun supportsParameter(parameter: MethodParameter): Boolean =
        TODO("Gjenkjenn @CurrentUser AuthenticatedUser")

    // TODO 2: Hent JwtAuthenticationToken, valider claims og map til intern modell.
    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any = TODO("Map principal og claims til AuthenticatedUser")
}

@Component
class CurrentUserWebConfig(private val resolver: CurrentUserArgumentResolver) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers += resolver
    }
}

@RestController
@RequestMapping("/api/profile")
class ProfileController {
    @GetMapping
    fun profile(@CurrentUser user: AuthenticatedUser): AuthenticatedUser = user
}

internal fun NativeWebRequest.jwtAuthentication(): JwtAuthenticationToken? =
    userPrincipal as? JwtAuthenticationToken
