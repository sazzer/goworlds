package uk.co.grahamcox.goworlds.service.http

import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.net.URI
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaMethod

/**
 * Helper to build a URI to a Controller method
 */
fun KFunction<*>.buildUri(vararg params: Any?): URI {
    val javaMethod = this.javaMethod!!
    val declaringClass = javaMethod.declaringClass

    return MvcUriComponentsBuilder.fromMethod(declaringClass, javaMethod, *params)
            .build()
            .toUri()
}
