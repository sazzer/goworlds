package uk.co.grahamcox.skl

/**
 * Field that represents a bind value
 */
data class BindField(val name: String) : Expression {
    /**
     * Build the SQL Snippit for this part of the query
     */
    override fun buildQueryString() = ":$name"
}
