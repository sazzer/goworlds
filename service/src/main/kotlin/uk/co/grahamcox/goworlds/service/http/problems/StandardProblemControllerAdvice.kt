package uk.co.grahamcox.goworlds.service.http.problems

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import uk.co.grahamcox.goworlds.service.http.problem.ProblemModel
import uk.co.grahamcox.goworlds.service.http.sorts.UnknownSortKeyException
import java.net.URI

/**
 * Controller Advice for handling standard problems
 */
@RestControllerAdvice
class StandardProblemControllerAdvice {
    /**
     * Handler for an Invalid Offset Exception
     */
    @ExceptionHandler(InvalidOffsetException::class)
    fun handleInvalidOffset() = ProblemModel(
            type = URI("tag:goworlds,2019:problems/invalid-offset"),
            title = "The specified offset was invalid",
            statusCode = HttpStatus.BAD_REQUEST
    )

    /**
     * Handler for an Invalid Count Exception
     */
    @ExceptionHandler(InvalidCountException::class)
    fun handleInvalidCount() = ProblemModel(
            type = URI("tag:goworlds,2019:problems/invalid-count"),
            title = "The specified count was invalid",
            statusCode = HttpStatus.BAD_REQUEST
    )

    /**
     * Handler for an Invalid Sort
     */
    @ExceptionHandler(UnknownSortKeyException::class)
    fun handleInvalidSort(e: UnknownSortKeyException) = ProblemModel(
            type = URI("tag:goworlds,2019:problems/invalid-sort"),
            title = "The specified sorts were invalid",
            statusCode = HttpStatus.BAD_REQUEST,
            details = InvalidSortsDetail(e.unknownSorts)
    )
}
