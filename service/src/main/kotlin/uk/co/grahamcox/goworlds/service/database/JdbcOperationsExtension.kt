package uk.co.grahamcox.goworlds.service.database

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import uk.co.grahamcox.goworlds.service.model.Page
import uk.co.grahamcox.skl.*
import java.sql.ResultSet

/**
 * Extension function to let us run a query for a single result
 *
 */
fun <T> NamedParameterJdbcOperations.queryForObject(query: Query, rowParser: (ResultSet) -> T) : T {
    return this.queryForObject(query.sql, query.binds) { rs, _ ->
        rowParser(rs)
    }!!
}
/**
 * Extension function to let us run a query for a page of results
 * @param selectBuilder The builder for the select statement
 * @param offset The offset of the rows to return
 * @param limit The limit of rows to return
 * @param rowParser The parser for the actual rows
 * @return the page of data
 */
fun <T> NamedParameterJdbcOperations.queryForPage(selectBuilder: SelectBuilder.() -> Unit,
                                                  offset: Long,
                                                  limit: Long,
                                                  rowParser: (ResultSet) -> T): Page<T> {
    val rowsSelect = select {
        selectBuilder(this)
        offset(offset)
        limit(limit)
    }
    val rows = this.query(rowsSelect.sql, rowsSelect.binds) { rs, _ -> rowParser(rs) }

    // If we returned 0 rows we might have run off the end of the resultset
    // If we returned exactly the requested limit of rows, we don't have enough information to know the total count
    // If we rerturned anything in between then we can calculate the total count without querying for it
    val count = if (rows.size == 0 || rows.size.toLong() == limit) {
        val countSelect = select {
            selectBuilder(this)

            noLimit()
            noOffset()
            resetOrderBy()
            resetSelecting()
            selecting(alias(Field("COUNT(*)"), "c"))
        }
        this.queryForObject(countSelect.sql, countSelect.binds, Long::class.java)!!
    } else {
        offset + rows.size
    }

    return Page(
            offset = offset,
            total = count,
            entries = rows
    )
}
