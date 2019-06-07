package uk.co.grahamcox.skl

/**
 * Query Builder that supports matching rows
 */
abstract class MatchingBuilder : QueryBuilder() {

    /** The list of where clauses */
    private val whereClauses: MutableList<Conditional> = mutableListOf()

    /**
     * Specify some conditionals to use in the WHERE clause
     */
    fun where(vararg conditional: Conditional) {
        whereClauses.addAll(conditional)
    }

    /**
     * Use a provided Handler to build a WHERE clause
     */
    fun where(handler: WhereBuilder.() -> Unit) {
        val builder = WhereBuilder()
        handler(builder)

        where(builder.build())
    }

    /**
     * Build the WHERE Clause
     */
    protected fun buildWhereClause() : String {
        val sql = StringBuilder()

        if (whereClauses.isNotEmpty()) {
            val whereString = CombiningConditional(whereClauses, "AND", false).buildQueryString()
            if (whereString.isNotBlank()) {
                sql.append(" WHERE ")
                sql.append(whereString)
            }
        }

        return sql.toString()
    }
}
