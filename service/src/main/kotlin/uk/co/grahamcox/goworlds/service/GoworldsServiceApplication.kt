package uk.co.grahamcox.goworlds.service

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import uk.co.grahamcox.goworlds.service.http.problem.ProblemResponseBodyAdvice
import uk.co.grahamcox.goworlds.service.http.problems.StandardProblemControllerAdvice
import uk.co.grahamcox.goworlds.service.oauth2.spring.OAuth2Config
import uk.co.grahamcox.goworlds.service.spring.WebMvcConfig
import uk.co.grahamcox.goworlds.service.users.spring.UsersConfig
import uk.co.grahamcox.goworlds.service.worlds.spring.WorldsConfig
import java.time.Clock

/**
 * The main application definition
 */
@SpringBootConfiguration
@EnableAutoConfiguration
@Import(
		UsersConfig::class,
		OAuth2Config::class,
		WorldsConfig::class,
		WebMvcConfig::class
)
class GoworldsServiceApplication(context: GenericApplicationContext) {
	init {
		beans {
			bean { Clock.systemUTC() }
			bean<ProblemResponseBodyAdvice>()
			bean<StandardProblemControllerAdvice>()
		}.initialize(context)
	}
}

/**
 * Entrypoint into the application
 */
fun main(args: Array<String>) {
	runApplication<GoworldsServiceApplication>(*args)
}
