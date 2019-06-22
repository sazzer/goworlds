package uk.co.grahamcox.goworlds.service.oauth2.http

import io.fusionauth.jwt.hmac.HMACSigner
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import uk.co.grahamcox.goworlds.service.database.getList
import uk.co.grahamcox.goworlds.service.database.getUUID
import uk.co.grahamcox.goworlds.service.model.Identity
import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientData
import uk.co.grahamcox.goworlds.service.oauth2.clients.ClientId
import uk.co.grahamcox.goworlds.service.oauth2.clients.GrantType
import uk.co.grahamcox.goworlds.service.oauth2.clients.ResponseType
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessToken
import uk.co.grahamcox.goworlds.service.oauth2.tokens.AccessTokenId
import uk.co.grahamcox.goworlds.service.password.HashedPassword
import uk.co.grahamcox.goworlds.service.users.UserData
import uk.co.grahamcox.goworlds.service.users.UserId
import java.time.Instant
import java.util.*

/**
 * Unit Tests for the ID Token Serializer
 */
internal class IdTokenSerializerImplTest {
    /** The test subject */
    private val testSubject = IdTokenSerializerImpl(HMACSigner.newSHA512Signer("secret"))

    /** The User ID */
    val userId = UserId(UUID.fromString("11111111-1111-1111-1111-111111111111"))

    /** The Client ID */
    val clientId = ClientId(UUID.fromString("22222222-2222-2222-2222-222222222222"))

    /** An Access Token */
    private val accessToken = AccessToken(
            id = AccessTokenId(UUID.fromString("00000000-0000-0000-0000-000000000000")),
            created = Instant.parse("2019-05-24T18:29:00Z"),
            expires = Instant.parse("2099-05-25T18:29:00Z"),
            user = userId,
            client = clientId,
            scopes = emptyList()
    )

    /** The User */
    private val user = Model(
            identity = Identity(
                    id = userId,
                    version = UUID.randomUUID(),
                    created = Instant.now(),
                    updated = Instant.now()
            ),
            data = UserData(
                    name = "Graham",
                    email = "graham@grahamcox.co.uk",
                    password = HashedPassword.hash("password")
            )
    )

    private val client = Model(
            identity = Identity(
                    id = clientId,
                    version = UUID.randomUUID(),
                    created = Instant.now(),
                    updated = Instant.now()
            ),
            data = ClientData(
                    name = "Test Client",
                    secret = HashedPassword("secret"),
                    owner = userId,
                    redirectUris = emptySet(),
                    responseTypes = emptySet(),
                    grantTypes = emptySet()
            )
    )

    @Test
    fun serialize() {
        val idToken = testSubject.serialize(user, client, accessToken)

        val jwt = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiIyMjIyMjIyMi0yMjIyLTIyMjItMjIyMi0yMjIyMjIyMjI" +
                "yMjIiLCJleHAiOjQwODM0MTY5NDAsImlhdCI6MTU1ODcyMjU0MCwiaXNzIjoiSWRUb2tlblNlcmlhbGl6ZXJJbXBsIiwic3ViIjoi" +
                "MTExMTExMTEtMTExMS0xMTExLTExMTEtMTExMTExMTExMTExIiwiYXV0aF90aW1lIjoxNTU4NzIyNTQwfQ.zEoxIBPeRFsAKzbUIu" +
                "IVhyze9Mjso0RdAE-MORxEPyjhbWQ7g2Tvxy1vfQU3lnSqtteumBDow_miOTYo3LtwDg"

        Assertions.assertEquals(jwt, idToken)
    }
}
