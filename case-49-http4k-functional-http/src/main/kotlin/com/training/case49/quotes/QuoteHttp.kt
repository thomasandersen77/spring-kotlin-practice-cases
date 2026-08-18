package com.training.case49.quotes

import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.then

data class QuoteRequest(val productCode: String, val quantity: Int)

data class QuoteResponse(val productCode: String, val quantity: Int, val totalOre: Long)

fun interface QuoteService {
	fun quote(request: QuoteRequest): QuoteResponse
}

// TODO 1: Lag lenses for request og response.
// TODO 2: Komponer routing for POST /quotes og map valideringsfeil til 400.
fun quoteRoutes(service: QuoteService): HttpHandler = TODO("Bygg funksjonell http4k-routing")

// TODO 3: Lag et Filter som gjenbruker/genererer X-Request-ID på alle responser.
fun requestIdFilter(): Filter = TODO("Implementer request-id-filter")

// TODO 4: Komponer dependencies eksplisitt uten container eller global tilstand.
fun quoteApp(service: QuoteService): HttpHandler = requestIdFilter().then(quoteRoutes(service))
