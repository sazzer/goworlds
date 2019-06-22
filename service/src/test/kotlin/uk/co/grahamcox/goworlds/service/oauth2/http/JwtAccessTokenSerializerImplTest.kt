package uk.co.grahamcox.goworlds.service.oauth2.http

import io.fusionauth.jwt.hmac.HMACSigner
import io.fusionauth.jwt.hmac.HMACVerifier
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import uk.co.grahamcox.goworlds.service.oauth2.OpenIDConnectScopes
import uk.co.grahamcox.goworlds.service.oauth2.ScopeRegistryImpl
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientId
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessToken
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessTokenId
import uk.co.grahamcox.goworlds.service.users.UserId
import java.time.Instant
import java.util.*

/**
 * Unit tests for the JWT Access Token Serializer
 */
internal class JwtAccessTokenSerializerImplTest {
    /** The test subject */
    private val testSubject = JwtAccessTokenSerializerImpl(
            HMACSigner.newSHA512Signer("secret"),
            HMACVerifier.newVerifier("secret"),
            ScopeRegistryImpl(OpenIDConnectScopes.values().toList()))

    /** An Access Token */
    private val accessToken = AccessToken(
            id = AccessTokenId(UUID.fromString("00000000-0000-0000-0000-000000000000")),
            created = Instant.parse("2019-05-24T18:29:00Z"),
            expires = Instant.parse("2099-05-25T18:29:00Z"),
            user = UserId(UUID.fromString("11111111-1111-1111-1111-111111111111")),
            client = ClientId(UUID.fromString("22222222-2222-2222-2222-222222222222")),
            scopes = emptyList()
    )

    @Test
    fun serializeNoScopes() {
        val jwt = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyMjIyMjIyMi0yMjIyLTIyMjItMjIyMi0yMjIyMjIyMjIy" +
                "MjIiLCJleHAiOjQwODM0MTY5NDAsImlhdCI6MTU1ODcyMjU0MCwiaXNzIjoiSnd0QWNjZXNzVG9rZW5TZXJpYWxpemVySW1" +
                "wbCIsIm5iZiI6MTU1ODcyMjU0MCwic3ViIjoiMTExMTExMTEtMTExMS0xMTExLTExMTEtMTExMTExMTExMTExIiwianRpIj" +
                "oiMDAwMDAwMDAtMDAwMC0wMDAwLTAwMDAtMDAwMDAwMDAwMDAwIiwic2NvcGVzIjpbXX0.7mYccqwB0nTM8YhX3ckgbufQX" +
                "CYLtUl8fH5Vka0W9J9uhFgZEjHwrZvdFe7--crlFfdUyQwbO0Llvh5R8XLoFA"

        Assertions.assertEquals(jwt, testSubject.serialize(accessToken))
    }

    @Test
    fun serializeWithScopes() {
        val accessToken = accessToken.copy(
                scopes = listOf(OpenIDConnectScopes.OPENID, OpenIDConnectScopes.EMAIL)
        )

        val jwt = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyMjIyMjIyMi0yMjIyLTIyMjItMjIyMi0yMjIyMjIyMjIy" +
                "MjIiLCJleHAiOjQwODM0MTY5NDAsImlhdCI6MTU1ODcyMjU0MCwiaXNzIjoiSnd0QWNjZXNzVG9rZW5TZXJpYWxpemVySW1" +
                "wbCIsIm5iZiI6MTU1ODcyMjU0MCwic3ViIjoiMTExMTExMTEtMTExMS0xMTExLTExMTEtMTExMTExMTExMTExIiwianRpIj" +
                "oiMDAwMDAwMDAtMDAwMC0wMDAwLTAwMDAtMDAwMDAwMDAwMDAwIiwic2NvcGVzIjpbIm9wZW5pZCIsImVtYWlsIl19.z0v3" +
                "qU_mgRU_Lf1MSTc6qhyeKabGkqlc7Yah4Ch2BopiWFYRI5tAononizof6b6gQogntKB8ShTECC7It3qYBg"

        Assertions.assertEquals(jwt, testSubject.serialize(accessToken))
    }

    @Test
    fun deserializeNoScopes() {
        val jwt = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyMjIyMjIyMi0yMjIyLTIyMjItMjIyMi0yMjIyMjIyMjIy" +
                "MjIiLCJleHAiOjQwODM0MTY5NDAsImlhdCI6MTU1ODcyMjU0MCwiaXNzIjoiSnd0QWNjZXNzVG9rZW5TZXJpYWxpemVySW1" +
                "wbCIsIm5iZiI6MTU1ODcyMjU0MCwic3ViIjoiMTExMTExMTEtMTExMS0xMTExLTExMTEtMTExMTExMTExMTExIiwianRpIj" +
                "oiMDAwMDAwMDAtMDAwMC0wMDAwLTAwMDAtMDAwMDAwMDAwMDAwIiwic2NvcGVzIjpbXX0.7mYccqwB0nTM8YhX3ckgbufQX" +
                "CYLtUl8fH5Vka0W9J9uhFgZEjHwrZvdFe7--crlFfdUyQwbO0Llvh5R8XLoFA"

        Assertions.assertEquals(accessToken, testSubject.deserialize(jwt))
    }

    @Test
    fun deserializeWithScopes() {
        val accessToken = accessToken.copy(
                scopes = listOf(OpenIDConnectScopes.OPENID, OpenIDConnectScopes.EMAIL)
        )

        val jwt = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyMjIyMjIyMi0yMjIyLTIyMjItMjIyMi0yMjIyMjIyMjIy" +
                "MjIiLCJleHAiOjQwODM0MTY5NDAsImlhdCI6MTU1ODcyMjU0MCwiaXNzIjoiSnd0QWNjZXNzVG9rZW5TZXJpYWxpemVySW1" +
                "wbCIsIm5iZiI6MTU1ODcyMjU0MCwic3ViIjoiMTExMTExMTEtMTExMS0xMTExLTExMTEtMTExMTExMTExMTExIiwianRpIj" +
                "oiMDAwMDAwMDAtMDAwMC0wMDAwLTAwMDAtMDAwMDAwMDAwMDAwIiwic2NvcGVzIjpbIm9wZW5pZCIsImVtYWlsIl19.z0v3" +
                "qU_mgRU_Lf1MSTc6qhyeKabGkqlc7Yah4Ch2BopiWFYRI5tAononizof6b6gQogntKB8ShTECC7It3qYBg"

        Assertions.assertEquals(accessToken, testSubject.deserialize(jwt))
    }

    @Test
    fun deserializeWithUnknownScopes() {
        val accessToken = accessToken.copy(
                scopes = listOf(OpenIDConnectScopes.OPENID, OpenIDConnectScopes.EMAIL)
        )

        val jwt = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyMjIyMjIyMi0yMjIyLTIyMjItMjIyMi0yMjIyMjIyMjIyM" +
                "jIiLCJleHAiOjQwODM0MTY5NDAsImlhdCI6MTU1ODcyMjU0MCwiaXNzIjoiSnd0QWNjZXNzVG9rZW5TZXJpYWxpemVySW1" +
                "wbCIsIm5iZiI6MTU1ODcyMjU0MCwic3ViIjoiMTExMTExMTEtMTExMS0xMTExLTExMTEtMTExMTExMTExMTExIiwianRpI" +
                "joiMDAwMDAwMDAtMDAwMC0wMDAwLTAwMDAtMDAwMDAwMDAwMDAwIiwic2NvcGVzIjpbIm9wZW5pZCIsImVtYWlsIiwidW5" +
                "rbm93biJdfQ.jyrVA_kF4MgLIrsSCTRMsrbFVMmmxWdcWdH6u-ZO-W7YuU4SIQGD77LfYBXQ-rldvqHg7v7IrErr5iacrCbHaQ"

        Assertions.assertEquals(accessToken, testSubject.deserialize(jwt))
    }
}
