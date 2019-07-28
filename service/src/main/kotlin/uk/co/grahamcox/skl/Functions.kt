package uk.co.grahamcox.skl

/**
 * Wrap the given expression in the "UPPER" function
 */
fun upper(expression: Expression) = Function("UPPER", expression)
