package uk.co.grahamcox.skl

/**
 * Details of an order by clause to apply
 */
data class OrderBy(
        val field: Expression,
        val sortOrder: SortOrder = SortOrder.ASCENDING
) : QueryPart {
    /**
     * Build the SQL Snippit for this part of the query
     */
    override fun buildQueryString(): String {
        val orderKeyword = when (sortOrder) {
            SortOrder.ASCENDING -> "ASC"
            SortOrder.DESCENDING -> "DESC"
        }

        return "${field.buildQueryString()} $orderKeyword"
    }
}
