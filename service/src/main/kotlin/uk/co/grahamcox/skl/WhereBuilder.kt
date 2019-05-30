package uk.co.grahamcox.skl

/**
 * Builder for the WHERE Clause
 */
class WhereBuilder {
    /** The conditions that have been built */
    private val conditions: MutableList<Conditional> = mutableListOf()

    /**
     * Add an arbitrary condition
     */
    fun condition(conditional: Conditional) {
        conditions.add(conditional)
    }

    /**
     * Add an Equality condition
     */
    fun eq(left: Expression, right: Expression) {
        condition(BiConditional(left, "=", right))
    }

    /**
     * Add an Greater Than condition
     */
    fun gt(left: Expression, right: Expression) {
        condition(BiConditional(left, ">", right))
    }

    /**
     * Add an Less Than condition
     */
    fun lt(left: Expression, right: Expression) {
        condition(BiConditional(left, "<", right))
    }

    /**
     * Add an Greater Than Or Equals condition
     */
    fun gte(left: Expression, right: Expression) {
        condition(BiConditional(left, ">=", right))
    }

    /**
     * Add an Less Than Or Equals condition
     */
    fun lte(left: Expression, right: Expression) {
        condition(BiConditional(left, "<=", right))
    }

    /**
     * Add an IS NULL Condition
     */
    fun isNull(value: Expression) {
        condition(NullConditional(value))
    }

    /**
     * Add an IS NOT NULL Condition
     */
    fun notNull(value: Expression) {
        condition(NotNullConditional(value))
    }
    /**
     * Build the Conditional that makes up this WHERE Clause
     */
    fun build() = CombiningConditional(conditions, "AND")
}
