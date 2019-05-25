package uk.co.grahamcox.goworlds.service.spring

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter

/**
 * Configure the Spring Web-MVC system
 */
@Configuration
class WebMvcConfig : WebMvcConfigurer {
    /** The interceptors to wire up */
    @Autowired
    private lateinit var interceptors: List<HandlerInterceptorAdapter>

    /**
     * Add all of our interceptors to the Interceptor Registry
     */
    override fun addInterceptors(registry: InterceptorRegistry) {
        interceptors.forEach { registry.addInterceptor(it) }
    }
}
