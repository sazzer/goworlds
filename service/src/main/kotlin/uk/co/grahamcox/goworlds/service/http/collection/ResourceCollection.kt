package uk.co.grahamcox.goworlds.service.http.collection

import java.net.URI

/**
 * Collection of resources returned over the HTTP API
 */
data class ResourceCollection<T>(
        val self: URI,
        val next: URI?,
        val previous: URI?,
        val offset: Long,
        val total: Long,
        val entries: List<T>
)
