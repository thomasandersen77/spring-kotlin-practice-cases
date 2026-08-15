package com.training.case53.legal

import com.openai.client.OpenAIClient
import com.openai.client.okhttp.OpenAIOkHttpClient
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@ConfigurationProperties("openai")
data class OpenAiProperties(
 var apiKey: String = "",
 var baseUrl: String = "https://api.openai.com/v1",
 var model: String = "gpt-5.6-luna",
 var maxOutputTokens: Long = 600
)

@Configuration
@EnableConfigurationProperties(OpenAiProperties::class)
class OpenAiConfiguration {
 @Bean
 fun openAiClient(properties: OpenAiProperties): OpenAIClient {
 require(properties.apiKey.isNotBlank()) {
 "OPENAI_API_KEY mangler. Se README for lokal .env.local-konfigurasjon."
 }
 return OpenAIOkHttpClient.builder()
 .apiKey(properties.apiKey)
 .baseUrl(properties.baseUrl)
 .build()
 }
}

/**
 * Transporttype for Structured Outputs. Offentlig, muterbar og med no-arg-konstruktør med vilje:
 * OpenAI SDK-et utleder JSON Schema og deserialiserer responsen fra denne Java-vennlige formen.
 */
class LegalReviewModelResponse {
 @JvmField var category: String = ""
 @JvmField var riskLevel: String = ""
 @JvmField var summary: String = ""
 @JvmField var evidence: String = ""
 @JvmField var missingInformation: List<String> = emptyList()
 @JvmField var requiresHumanReview: Boolean = true
}

@Component
class OpenAiContractReviewAdapter(
 private val client: OpenAIClient,
 private val properties: OpenAiProperties
) : ContractReviewPort {
 // TODO nivå 2:
 // 1. Bygg ResponseCreateParams med instructions, input, model og maxOutputTokens.
 // 2. Be om Structured Outputs med LegalReviewModelResponse::class.java.
 // 3. Finn nøyaktig ett output_text-element og map det til ModelReviewProposal.
 // 4. Skill avslag/ugyldig respons fra rate limit, timeout og 5xx.
 override fun review(prompt: LegalReviewPrompt): ModelReviewProposal =
 TODO("Kall OpenAI Responses API med den offisielle Java SDK-en")
}
