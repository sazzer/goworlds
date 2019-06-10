package uk.co.grahamcox.goworlds.service.oauth2.http

import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import uk.co.grahamcox.goworlds.service.oauth2.AccessTokenHolder
import uk.co.grahamcox.goworlds.service.oauth2.authorization.Authorizer
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessToken
import uk.co.grahamcox.goworlds.service.users.UserId
import kotlin.reflect.jvm.kotlinFunction

/**
 * Argument Resolver to make Access Token details available to the controllers
 */
class AccessTokenArgumentResolver(
        private val accessTokenHolder: AccessTokenHolder
) : HandlerMethodArgumentResolver {
    companion object {
        private val PARAMETER_MAPPING = mapOf(
                AccessToken::class.java to { accessToken : AccessToken? -> accessToken },
                UserId::class.java to { accessToken : AccessToken? -> accessToken?.user },
                Authorizer::class.java to { accessToken : AccessToken? ->
                    Authorizer(accessToken ?: throw MissingAccessTokenException())
                }
        )
    }
    /**
     * Check if we can support this parameter
     * @return True if the parameter is supported. False if not
     */
    override fun supportsParameter(parameter: MethodParameter) = PARAMETER_MAPPING.containsKey(parameter.parameterType)

    /**
     * Actually handle the argument resolution
     */
    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? {
        val parameterIndex = parameter.parameterIndex
        val nullable = parameter.method?.kotlinFunction!!.parameters[parameterIndex + 1].type.isMarkedNullable

        val accessToken = accessTokenHolder.accessToken
        if (!nullable && accessToken == null) {
            throw MissingAccessTokenException()
        }

        return PARAMETER_MAPPING[parameter.parameterType]?.invoke(accessToken)
    }
}
