package uk.co.grahamcox.goworlds.service.password

import java.security.SecureRandom
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * Representation of a hashed password
 */
data class HashedPassword(val hash: String) {
    companion object {
        /** The iterations to use */
        private const val ITERATIONS = 65536

        /** The key length to use */
        private const val KEY_LENGTH = 512

        /** The salt size to use */
        private const val SALT_SIZE = 32

        /** The algorithm to use */
        private const val ALGORITHM = "PBKDF2WithHmacSHA1"

        /**
         * Hash the given plaintext password
         * @param input The input to hash
         * @return the hashed password
         */
        fun hash(input: String) : HashedPassword {
            val random = SecureRandom()
            val salt = ByteArray(SALT_SIZE)
            random.nextBytes(salt)

            return hash(salt, input)
        }

        /**
         * Hash the password with the given salt
         * @param salt The salt to use
         * @param input The input to hash
         * @return the hashed password
         */
        private fun hash(salt: ByteArray, input: String) : HashedPassword {
            val spec = PBEKeySpec(input.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
            val factory = SecretKeyFactory.getInstance(ALGORITHM)

            val hash = factory.generateSecret(spec).encoded

            val encoder = Base64.getEncoder()
            val encodedHash = encoder.encodeToString(hash)
            val encodedSalt = encoder.encodeToString(salt)

            return HashedPassword("$encodedSalt,$encodedHash")
        }
    }

    /**
     * Check if the given plaintext matches this hash
     * @param plaintext The plaintext to check
     * @return true if the passwords match. False if not
     */
    fun check(plaintext: String) : Boolean {
        val (encodedSalt, _) = hash.split(",")
        val salt = Base64.getDecoder().decode(encodedSalt)

        val rehash = hash(salt, plaintext)

        return equals(rehash)
    }
}
