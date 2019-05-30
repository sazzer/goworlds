package uk.co.grahamcox.skl

/**
 * Conditional that has two components - a left and right - and a condition joining them
 */
data class BiConditional(
        val left: Expression,
        val condition: String,
        val right: Expression
) : Conditional {
    /**
     * Build the SQL Snippit for this part of the query
     */
    override fun buildQueryString() = "${left.buildQueryString()} $condition ${right.buildQueryString()}"
}
