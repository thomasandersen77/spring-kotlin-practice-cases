package com.training.case45.documents

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@SpringBootApplication
@EnableMethodSecurity
class DocumentSecurityApplication

fun main(args: Array<String>) {
 runApplication<DocumentSecurityApplication>(*args)
}

data class Document(val id: Long, val owner: String, val classification: Classification)
enum class Classification { INTERNAL, CONFIDENTIAL }

fun interface DocumentRepository {
 fun findById(id: Long): Document?
}

@Component("documentAccessPolicy")
class DocumentAccessPolicy(private val repository: DocumentRepository) {
 // TODO 1: Eier kan lese INTERNAL. REVIEWER kan lese alle. CONFIDENTIAL krever AUDITOR.
 fun canRead(authentication: Authentication, documentId: Long): Boolean =
 TODO("Implementer domenerelevant access policy")
}

class DocumentNotFound(id: Long) : RuntimeException("Dokument $id finnes ikke")

@Service
class DocumentService(private val repository: DocumentRepository) {
 // TODO 2: Begrunn om policyen hører i uttrykket, servicekoden eller en kombinasjon.
 @PreAuthorize("@documentAccessPolicy.canRead(authentication, #id)")
 fun get(id: Long): Document = repository.findById(id) ?: throw DocumentNotFound(id)
}
