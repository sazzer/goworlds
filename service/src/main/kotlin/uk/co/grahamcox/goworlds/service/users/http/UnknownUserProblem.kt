package uk.co.grahamcox.goworlds.service.users.http

import org.springframework.http.HttpStatus
import uk.co.grahamcox.goworlds.service.http.problem.ProblemModel
import java.net.URI

/**
 * Construct an Unknown User Problem to return
 */
fun unknownUserProblem() = ProblemModel(
        type = URI("tag:goworlds,2019:users/problems/unknown-user"),
        title = "The requested user could not be found",
        statusCode = HttpStatus.NOT_FOUND
)
