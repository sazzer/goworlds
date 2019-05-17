package uk.co.grahamcox.goworlds.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GoworldsServiceApplication

fun main(args: Array<String>) {
	runApplication<GoworldsServiceApplication>(*args)
}
