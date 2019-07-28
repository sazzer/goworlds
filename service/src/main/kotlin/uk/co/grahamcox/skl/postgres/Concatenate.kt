package uk.co.grahamcox.skl.postgres

import uk.co.grahamcox.skl.Expression
import uk.co.grahamcox.skl.QueryPart

/**
 * Wrap a number of query parts together so that they get concatenated
 */
data class Concatenate(val parts: List<Expression>) : Expression {

    constructor(vararg parts: Expression) : this(parts.toList())

    /**
     * Build the SQL Snippit for this part of the query
     */
    override fun buildQueryString() = parts.joinToString(
            separator = "||",
            transform = QueryPart::buildQueryString
    )
}