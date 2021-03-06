package uk.co.grahamcox.skl

/**
 * Conditional that combines several others together in a chain
 */
data class CombiningConditional(val conditions: List<Conditional>,
                                val combining: String,
                                val brackets: Boolean = true) : Conditional {
    /**
     * Build the SQL Snippit for this part of the query
     */
    override fun buildQueryString() =
            if (conditions.isEmpty()) {
                ""
            } else {
                val queries = conditions.map(Conditional::buildQueryString)
                        .map(String::trim)
                        .filter(String::isNotBlank)

                queries.joinToString(separator = " $combining ",
                        prefix = when (brackets) {
                            true -> "("
                            false -> ""
                        },
                        postfix = when (brackets) {
                            true -> ")"
                            false -> ""
                        })
            }
}
