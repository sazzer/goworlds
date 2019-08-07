package uk.co.grahamcox.skl

/**
 * Base class for building queries
 */
abstract class QueryBuilder {
    /** The bind values */
    protected val bindValues: MutableMap<String, Any?> = mutableMapOf()

    /**
     * Generate a Bind expression for the given value
     */
    fun bind(value: Any?): Expression {
        val bindKey = "bind${bindValues.size}"
        bindValues[bindKey] = value

        return BindField(bindKey)
    }

    /**
     * Wrap an expression with an alias
     */
    fun alias(expression: Expression, alias: String) = ExpressionAlias(expression, alias)

    /**
     * Actually build the query
     */
    abstract fun build(): Query
}
