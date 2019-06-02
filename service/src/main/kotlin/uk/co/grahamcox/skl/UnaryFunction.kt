package uk.co.grahamcox.skl

/**
 * Wrap an expression in a call to a function - e.g. UPPER
 */
data class UnaryFunction(val function: String, val expression: Expression) : Expression {
    /**
     * Build the SQL Snippit for this part of the query
     */
    override fun buildQueryString() = "$function(${expression.buildQueryString()})"
}
