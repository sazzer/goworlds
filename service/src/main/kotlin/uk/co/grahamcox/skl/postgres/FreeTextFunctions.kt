package uk.co.grahamcox.skl.postgres

import uk.co.grahamcox.skl.*
import uk.co.grahamcox.skl.Function

/**
 * Wrap an expression in a call to to_tsvector
 */
fun toTsVector(value: Expression) = Function("to_tsvector", value)

/**
 * Wrap an expression in a call to setweight, optionally calling to_tsvector on the value first
 */
fun setWeight(input: Expression, weight: String, wrap: Boolean = false): Expression {
    val inner = if (wrap) {
        toTsVector(input)
    } else {
        input
    }

    return Function("setweight", inner, Constant(weight))
}

/**
 * Enumeration of possible forms for the to_tsquery call
 */
enum class TsQueryForm {
    DEFAULT,
    PLAIN,
    WEB_SEARCH
}

/**
 * Wrap an expression in a call to to_tsquery, plainto_tsquery or websearch_to_tsquery as appropriate
 */
fun toTsQuery(input: Expression, form: TsQueryForm) = Function(
        when(form) {
            TsQueryForm.DEFAULT -> "to_tsquery"
            TsQueryForm.PLAIN -> "plainto_tsquery"
            TsQueryForm.WEB_SEARCH -> "websearch_to_tsquery"
        },
        input)

/**
 * Wrap a Text Search Vector and Query in a call to ts_rank_cd
 */
fun tsRankCd(vector: Expression, query: Expression) = Function(
        "ts_rank_cd",
        vector,
        query
)

/**
 * Produce a conditional for a Text Search Matches condition
 */
fun matches(vector: Expression, query: Expression) = BiConditional(vector, "@@", query)

/**
 * Produce a conditional for a Text Search Matches condition
 */
fun WhereBuilder.matches(vector: Expression, query: Expression) {
    this.condition(BiConditional(vector, "@@", query))
}