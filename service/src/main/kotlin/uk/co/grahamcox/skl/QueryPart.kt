package uk.co.grahamcox.skl

/**
 * Interface identifying any part of a Query that can generate it's own SQL snippit
 */
interface QueryPart {
    /**
     * Build the SQL Snippit for this part of the query
     */
    fun buildQueryString() : String
}
