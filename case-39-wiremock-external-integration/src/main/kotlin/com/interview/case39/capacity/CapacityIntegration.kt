package com.interview.case39.capacity

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestClient

@SpringBootApplication class CapacityApplication
fun main(args: Array<String>) { runApplication<CapacityApplication>(*args) }

data class ExternalCapacityResponse(@JsonProperty("consultant_ref") val consultantRef: String, @JsonProperty("state_code") val stateCode: String, val periods: List<ExternalPeriod>?, val skills: List<String>?)
data class ExternalPeriod(@JsonProperty("from_date") val fromDate: String, @JsonProperty("to_date") val toDate: String, @JsonProperty("capacity_pct") val capacityPercent: Int)

sealed interface Availability { data object Available : Availability; data object PartiallyAvailable : Availability; data object Unavailable : Availability }
data class CapacityPeriod(val from: String, val to: String, val percent: Int)
data class ConsultantCapacity(val consultantId: String, val availability: Availability, val periods: List<CapacityPeriod>, val skills: List<String>)
data class CapacityResponse(val consultantId: String, val status: String, val nextCapacityPercent: Int?, val skills: List<String>)

class ConsultantNotFound(id: String) : RuntimeException("Konsulent $id finnes ikke")
class ProviderUnavailable(cause: Throwable? = null) : RuntimeException("Leverandør utilgjengelig", cause)
class InvalidProviderResponse(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

// TODO 1–2: Oversett leverandørens semantikk og valider transportdata.
fun ExternalCapacityResponse.toDomain(): ConsultantCapacity = TODO("Map ekstern DTO gjennom ACL")

fun interface CapacityPort { fun getCapacity(consultantId: String): ConsultantCapacity }

@Configuration
class RestClientConfig {
    @Bean fun capacityRestClient(@Value("\${provider.capacity.base-url}") baseUrl: String): RestClient = RestClient.builder().baseUrl(baseUrl).build()
}

@Component
class RestClientCapacityAdapter(private val restClient: RestClient, @Value("\${provider.capacity.api-key}") private val apiKey: String) : CapacityPort {
    // TODO 3–4: GET /external/consultants/{id}/capacity, X-Api-Key og presis feiloversettelse.
    override fun getCapacity(consultantId: String): ConsultantCapacity = TODO("Kall leverandør og map respons/feil")
}

@Service
class CapacityService(private val port: CapacityPort) {
    // TODO 5–6: Bruk porten og map intern modell til stabil DTO.
    fun get(consultantId: String): CapacityResponse = TODO("Orkestrer og map response")
}

@RestController @RequestMapping("/api/capacity")
class CapacityController(private val service: CapacityService) {
    @GetMapping("/{consultantId}") fun get(@PathVariable consultantId: String) = service.get(consultantId)
}

@RestControllerAdvice
class CapacityErrorHandler {
    @ExceptionHandler(ConsultantNotFound::class) @ResponseStatus(HttpStatus.NOT_FOUND) fun notFound(ex: ConsultantNotFound) = mapOf("error" to ex.message)
    @ExceptionHandler(ProviderUnavailable::class) @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE) fun unavailable(ex: ProviderUnavailable) = mapOf("error" to ex.message)
    @ExceptionHandler(InvalidProviderResponse::class) @ResponseStatus(HttpStatus.BAD_GATEWAY) fun invalid(ex: InvalidProviderResponse) = mapOf("error" to ex.message)
}
