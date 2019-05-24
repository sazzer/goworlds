package uk.co.grahamcox.goworlds.service.oauth2.http

import io.fusionauth.jwt.hmac.HMACSigner
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import uk.co.grahamcox.goworlds.service.oauth2.OpenIDConnectScopes
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
    private val testSubject = JwtAccessTokenSerializerImpl(HMACSigner.newSHA512Signer("secret"))

    @Test
    fun serializeNoScopes() {
        val serialized = testSubject.serialize(AccessToken(
                id = AccessTokenId(UUID.fromString("00000000-0000-0000-0000-000000000000")),
                created = Instant.parse("2019-05-24T18:29:00Z"),
                expires = Instant.parse("2019-05-25T18:29:00Z"),
                user = UserId(UUID.fromString("11111111-1111-1111-1111-111111111111")),
                client = ClientId(UUID.fromString("22222222-2222-2222-2222-222222222222")),
                scopes = emptyList()
        ))

        Assertions.assertEquals("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyMjIyMjIyMi0yMjIyLTIy" +
                "MjItMjIyMi0yMjIyMjIyMjIyMjIiLCJleHAiOjE1NTg4MDg5NDAsImlhdCI6MTU1ODcyMjU0MCwiaXNzIjoiSnd0QWNjZX" +
                "NzVG9rZW5TZXJpYWxpemVySW1wbCIsIm5iZiI6MTU1ODcyMjU0MCwic3ViIjoiMTExMTExMTEtMTExMS0xMTExLTExMTEt" +
                "MTExMTExMTExMTExIiwianRpIjoiMDAwMDAwMDAtMDAwMC0wMDAwLTAwMDAtMDAwMDAwMDAwMDAwIiwic2NvcGVzIjpbXX" +
                "0.GcxiraO-UUFX-47Xfvl_nsBfqcdgbQUKuW3eZCxeRlesaRf0dyMNMFPIvGQnYvf7UAgF8PdAypy4FNV5mskTww",
                serialized)
    }

    @Test
    fun serializeWithScopes() {
        val serialized = testSubject.serialize(AccessToken(
                id = AccessTokenId(UUID.fromString("00000000-0000-0000-0000-000000000000")),
                created = Instant.parse("2019-05-24T18:29:00Z"),
                expires = Instant.parse("2019-05-25T18:29:00Z"),
                user = UserId(UUID.fromString("11111111-1111-1111-1111-111111111111")),
                client = ClientId(UUID.fromString("22222222-2222-2222-2222-222222222222")),
                scopes = listOf(OpenIDConnectScopes.OPENID, OpenIDConnectScopes.EMAIL)
        ))

        Assertions.assertEquals("eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyMjIyMjIyMi0yMjIyLTIy" +
                "MjItMjIyMi0yMjIyMjIyMjIyMjIiLCJleHAiOjE1NTg4MDg5NDAsImlhdCI6MTU1ODcyMjU0MCwiaXNzIjoiSnd0QWNjZX" +
                "NzVG9rZW5TZXJpYWxpemVySW1wbCIsIm5iZiI6MTU1ODcyMjU0MCwic3ViIjoiMTExMTExMTEtMTExMS0xMTExLTExMTEt" +
                "MTExMTExMTExMTExIiwianRpIjoiMDAwMDAwMDAtMDAwMC0wMDAwLTAwMDAtMDAwMDAwMDAwMDAwIiwic2NvcGVzIjpbIm" +
                "9wZW5pZCIsImVtYWlsIl19.O6xXlt5VDgt4oNYaE0P6SNe7o4DMh1NJptjMv3iJthYIVMbv6CMHnmpVqE52IZqq4ywKzmD" +
                "ur3et2GEvz73yoQ",
                serialized)
    }
}
