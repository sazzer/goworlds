package uk.co.grahamcox.skl

/**
 * Representation of a Constant expression.
 * TODO: Escaping of values
 */
data class Constant(val value: Any?) : Expression {
    /**
     * Build the SQL Snippit for this part of the query
     */
    override fun buildQueryString() = when(value) {
        null -> "NULL"
        is String -> "'$value'"
        true -> "TRUE"
        false -> "FALSE"
        else -> value.toString()
    }
}
