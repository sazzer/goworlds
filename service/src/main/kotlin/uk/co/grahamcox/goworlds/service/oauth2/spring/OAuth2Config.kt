package uk.co.grahamcox.goworlds.service.oauth2.spring

import io.fusionauth.jwt.hmac.HMACSigner
import io.fusionauth.jwt.hmac.HMACVerifier
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.core.env.get
import uk.co.grahamcox.goworlds.service.oauth2.OpenIDConnectScopes
import uk.co.grahamcox.goworlds.service.oauth2.ScopeRegistry
import uk.co.grahamcox.goworlds.service.oauth2.ScopeRegistryImpl
import uk.co.grahamcox.goworlds.service.oauth2.clients.dao.ClientJdbcDao
import uk.co.grahamcox.goworlds.service.oauth2.http.*
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessTokenGenerator
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessTokenGeneratorImpl
import java.time.Duration
import java.time.Period

/**
 * Spring Configuration for working with OAuth2 Details
 */
@Configuration
class OAuth2Config(context: GenericApplicationContext) {
    init {
        beans {
            bean<ClientJdbcDao>()
            bean<ScopeRegistry> {
                ScopeRegistryImpl(
                        listOf(
                            OpenIDConnectScopes.values().asList()
                        ).flatten()
                )
            }
            bean<ClientCredentialsGrantTypeHandler>()
            bean<EmailExtensionGrantTypeHandler>()
            bean<AccessTokenGenerator> {
                AccessTokenGeneratorImpl(ref(), Duration.parse(env["goworlds.oauth2.token.duration"]))
            }
            bean<AccessTokenSerializer> {
                val hmacSecret = env["goworlds.oauth2.token.secret"]

                JwtAccessTokenSerializerImpl(
                        HMACSigner.newSHA512Signer(hmacSecret),
                        HMACVerifier.newVerifier(hmacSecret),
                        ref()
                )
            }
            bean {
                OAuth2TokenController(
                        ref(),
                        ref(),
                        mapOf(
                                "client_credentials" to ref<ClientCredentialsGrantTypeHandler>(),
                                "tag:goworlds,2019:oauth2/grant_type/email_password" to ref<EmailExtensionGrantTypeHandler>())
                )
            }
            bean<AccessTokenStore>()
            bean<AccessTokenInterceptor>()
            bean<AccessTokenArgumentResolver>()
            bean<AccessTokenController>()
            bean<AccessTokenControllerAdvice>()
        }.initialize(context)
    }
}
