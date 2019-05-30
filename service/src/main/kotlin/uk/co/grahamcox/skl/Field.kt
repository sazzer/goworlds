package uk.co.grahamcox.skl

/**
 * Representation of a field from a table
 */
data class Field(
        val field: String,
        val table: Table? = null
) : Expression {
    /**
     * Build the SQL Snippit for this part of the query
     */
    override fun buildQueryString() = when(table) {
        null -> field
        else -> "${table.key}.$field"
    }
}
