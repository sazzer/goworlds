package uk.co.grahamcox.goworlds.service

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import
import uk.co.grahamcox.goworlds.service.oauth2.spring.OAuth2Config
import uk.co.grahamcox.goworlds.service.users.spring.UsersConfig

/**
 * The main application definition
 */
@SpringBootConfiguration
@EnableAutoConfiguration
@Import(
		UsersConfig::class,
		OAuth2Config::class
)
class GoworldsServiceApplication

/**
 * Entrypoint into the application
 */
fun main(args: Array<String>) {
	runApplication<GoworldsServiceApplication>(*args)
}
