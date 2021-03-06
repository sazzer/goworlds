package uk.co.grahamcox.skl

/**
 * Builder class for building Select statements
 */
class SelectBuilder : MatchingBuilder() {
    /** The list of tables to select from */
    private val selectTables: MutableList<Table> = mutableListOf()

    /** The list of select clauses */
    private val selectClauses: MutableList<Expression> = mutableListOf()

    /** The list of order by clauses */
    private val orderClauses: MutableList<OrderBy> = mutableListOf()

    /** The limit to apply */
    private var limit: Long? = null

    /** The offset to apply */
    private var offset: Long? = null

    /**
     * Add the tables to select from
     * @param tables The tables
     */
    fun from(vararg tables: String) : List<Table> {
        return this.from(*tables.map { Table(it) }.toTypedArray())
    }

    /**
     * Add the tables to select from
     * @param tables The tables
     */
    fun from(vararg tables: Pair<String, String>) : List<Table> {
        return this.from(*tables.map { Table(it.first, it.second) }.toTypedArray())
    }

    /**
     * Add the tables to select from
     * @param tables The tables
     */
    fun from(vararg tables: Table) : List<Table> {
        this.selectTables.addAll(tables.map { it })
        return tables.toList()
    }

    /**
     * Define a limit on the rows to return
     * @return the limit
     */
    fun limit(limit: Long) {
        this.limit = limit
    }

    /**
     * Define an offset on the rows to return
     * @return the offset
     */
    fun offset(offset: Long) {
        this.offset = offset
    }

    /**
     * Remove the limit on the rows to return
     */
    fun noLimit() {
        this.limit = null
    }

    /**
     * Remove the offset on the rows to return
     */
    fun noOffset() {
        this.offset = null
    }

    /**
     * Specify expressions to return
     */
    fun selecting(vararg expression: Expression) {
        selectClauses.addAll(expression)
    }

    /**
     * Reset the select clauses to return
     */
    fun resetSelecting() {
        selectClauses.clear()
    }

    /**
     * Add an Order By clause
     */
    fun orderBy(expression: Expression) {
        orderBy(expression, SortOrder.ASCENDING)
    }

    /**
     * Add an Order By clause
     */
    fun orderBy(expression: Expression, dir: SortOrder) {
        orderBy(OrderBy(expression, dir))
    }

    /**
     * Add some Order By clauses
     */
    fun orderBy(vararg orderBy: OrderBy) {
        orderClauses.addAll(orderBy)
    }

    /**
     * Reset the Order By clauses to apply
     */
    fun resetOrderBy() {
        orderClauses.clear()
    }

    /**
     * Actually build the query
     */
    override fun build() : Query {
        val sql = StringBuilder()

        sql.append("SELECT ")

        // The fields to return
        if (selectClauses.isEmpty()) {
            sql.append("*")
        } else {
            sql.append(selectClauses.joinToString(", ", transform = QueryPart::buildQueryString))
        }

        // The tables to select from
        if (selectTables.isNotEmpty()) {
            sql.append(" FROM ")
            sql.append(selectTables.joinToString(", ", transform = QueryPart::buildQueryString))
        }

        // The Where Clause, if any
        sql.append(buildWhereClause())

        // The Order By Clause, if any
        if (orderClauses.isNotEmpty()) {
            sql.append(" ORDER BY ")
            sql.append(orderClauses.joinToString(", ", transform = QueryPart::buildQueryString))
        }

        // Limits and Offset, if any
        limit?.run { sql.append(" LIMIT $limit") }
        offset?.run { sql.append(" OFFSET $offset") }

        return Query(sql.toString(), bindValues)
    }
}

/**
 * DSL Starting point for a SELECT statement
 * @param builder The builder lambda for building the query
 * @return the build query
 */
fun select(builder: SelectBuilder.() -> Unit) : Query {
    val selectBuilder = SelectBuilder()
    builder(selectBuilder)
    return selectBuilder.build()
}
