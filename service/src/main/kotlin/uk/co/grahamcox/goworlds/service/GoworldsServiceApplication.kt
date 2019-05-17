package uk.co.grahamcox.goworlds.service

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.runApplication

/**
 * The main application definition
 */
@SpringBootConfiguration
@EnableAutoConfiguration
class GoworldsServiceApplication

/**
 * Entrypoint into the application
 */
fun main(args: Array<String>) {
	runApplication<GoworldsServiceApplication>(*args)
}
