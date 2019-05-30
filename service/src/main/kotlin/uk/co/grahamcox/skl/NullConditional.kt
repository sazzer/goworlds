package uk.co.grahamcox.skl

/**
 * Conditional to check if an expression is null
 */
data class NullConditional(val value: Expression) : Conditional {
    /**
     * Build the SQL Snippit for this part of the query
     */
    override fun buildQueryString() = "${value.buildQueryString()} IS NULL"
}
