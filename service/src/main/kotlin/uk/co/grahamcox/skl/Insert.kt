package uk.co.grahamcox.skl

/**
 * Builder class for building Insert statements
 */
class InsertBuilder(private val into: Table) : QueryBuilder() {
    /** The values to set */
    private val values: MutableList<Pair<String, Expression>> = mutableListOf()

    /** The values to return */
    private val returning: MutableList<Expression> = mutableListOf()

    /** Indicate to return everything */
    private var returnAll: Boolean = false

    /**
     * Set the given field to the given value
     */
    fun set(field: String, value: Expression) {
        values.add(field to value)
    }

    /**
     * Indicate to return everything
     */
    fun returnAll() {
        returnAll = true
    }

    /**
     * Indicate to return the given value
     */
    fun returning(value: Expression) {
        returning.add(value)
    }

    /**
     * Indicate to return the given value
     */
    fun returning(field: String) {
        returning(into.field(field))
    }

    /**
     * Actually build the query
     */
    override fun build(): Query {
        val sql = StringBuilder()

        sql.append("INSERT INTO ")
        sql.append(into.buildQueryString())

        sql.append(values.joinToString(separator = ",", prefix = "(", postfix = ")",
                transform = Pair<String, Expression>::first))

        sql.append(" VALUES ")
        sql.append(values.joinToString(separator = ",", prefix = "(", postfix = ")") {
            it.second.buildQueryString()
        })

        if (returnAll) {
            sql.append(" RETURNING *")
        } else if (returning.isNotEmpty()) {
            sql.append(" RETURNING ")
            sql.append(returning.joinToString(separator = ",", transform = QueryPart::buildQueryString))
        }

        return Query(sql.toString(), bindValues)
    }
}

/**
 * DSL Starting point for a INSERT statement
 * @param builder The builder lambda for building the query
 * @return the build query
 */
fun insert(into: String, builder: InsertBuilder.() -> Unit) : Query {
    return insert(Table(into), builder)
}

/**
 * DSL Starting point for a INSERT statement
 * @param builder The builder lambda for building the query
 * @return the build query
 */
fun insert(into: Table, builder: InsertBuilder.() -> Unit) : Query {
    val insertBuilder = InsertBuilder(into)
    builder(insertBuilder)
    return insertBuilder.build()
}
