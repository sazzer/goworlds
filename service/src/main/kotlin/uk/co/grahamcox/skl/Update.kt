package uk.co.grahamcox.skl

/**
 * Builder class for building Update statements
 */
class UpdateBuilder(private val into: Table) : MatchingBuilder() {
    /** The values to set */
    private val values: MutableList<Pair<String, Expression>> = mutableListOf()

    /** The values to return */
    private val returning: MutableList<Expression> = mutableListOf()

    /** Indicate to return everything */
    private var returnAll: Boolean = false

    /**
     * Get the field definition for the named column
     */
    fun field(field: String) = into.field(field)

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

        sql.append("UPDATE ")
        sql.append(into.buildQueryString())

        sql.append(" SET ")
        sql.append(values.joinToString(separator = ",") {
            "${it.first} = ${it.second.buildQueryString()}"
        })

        sql.append(buildWhereClause())

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
 * DSL Starting point for a UPDATE statement
 * @param builder The builder lambda for building the query
 * @return the build query
 */
fun update(into: String, builder: UpdateBuilder.() -> Unit) : Query {
    return update(Table(into), builder)
}

/**
 * DSL Starting point for a UPDATE statement
 * @param builder The builder lambda for building the query
 * @return the build query
 */
fun update(into: Table, builder: UpdateBuilder.() -> Unit) : Query {
    val updateBuilder = UpdateBuilder(into)
    builder(updateBuilder)
    return updateBuilder.build()
}
