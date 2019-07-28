package uk.co.grahamcox.skl

/**
 * Represent a call to a function with some expressions as parameters - e.g. UPPER
 */
class Function(val function: String, vararg val parameters: Expression) : Expression {
    /**
     * Build the SQL Snippit for this part of the query
     */
    override fun buildQueryString() =
            parameters.joinToString(
                    separator = ",",
                    prefix = "$function(",
                    postfix = ")",
                    transform = Expression::buildQueryString
            )
}
