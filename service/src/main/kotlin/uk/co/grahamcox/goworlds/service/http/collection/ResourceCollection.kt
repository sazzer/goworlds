package uk.co.grahamcox.goworlds.service.http.collection

/**
 * Collection of resources returned over the HTTP API
 */
data class ResourceCollection<T>(
        val offset: Long,
        val total: Long,
        val entries: List<T>
)
