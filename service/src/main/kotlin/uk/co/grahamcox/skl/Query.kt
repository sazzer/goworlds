package uk.co.grahamcox.skl

/**
 * Representation of a Query
 * @property sql The SQL for the query
 * @property binds The binds for the query
 */
data class Query(
        val sql: String,
        val binds: Map<String, Any?>
)
