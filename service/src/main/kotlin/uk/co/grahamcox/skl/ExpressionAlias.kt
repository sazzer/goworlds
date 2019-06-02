package uk.co.grahamcox.skl

/**
 * Wrapper around an expression to give it an alias
 */
data class ExpressionAlias(val expression: Expression, val alias: String) : Expression {
    /**
     * Build the SQL Snippit for this part of the query
     */
    override fun buildQueryString() = "${expression.buildQueryString()} AS $alias"
}
