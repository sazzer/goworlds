package uk.co.grahamcox.skl

/**
 * Represent a call to a function with some expressions as parameters - e.g. UPPER
 */
data class Function(val function: String, val parameters: List<Expression>) : Expression {
    constructor(function: String, vararg parameters: Expression) : this(function, parameters.toList())

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
