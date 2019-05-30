package uk.co.grahamcox.skl

/**
 * Representation of a Table in a statement
 */
data class Table(
        val name: String,
        val alias: String? = null
) : QueryPart {
    /**
     * Build the SQL Snippit for this part of the query
     */
    override fun buildQueryString() = when (alias) {
        null -> name
        else -> "$name AS $alias"
    }

    /** The key for the table - which is the alias if set or the name otherwise */
    val key = alias ?: name

    /**
     * Get a Field from this Table
     */
    fun field(field: String) = Field(field, this)

    /**
     * Helper to allow square brackets to return table fields
     */
    operator fun get(field: String) = field(field)
}
