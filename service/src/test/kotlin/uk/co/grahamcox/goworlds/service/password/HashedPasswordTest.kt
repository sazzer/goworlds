package uk.co.grahamcox.goworlds.service.password

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.function.Executable

internal class HashedPasswordTest {
    @Test
    fun hashPassword() {
        val input = "input"
        val hash = HashedPassword.hash(input)

        Assertions.assertAll(
                Executable { Assertions.assertNotEquals("", hash.hash) },
                Executable { Assertions.assertNotEquals(input, hash.hash) }
        )
    }

    @Test
    fun hashPasswordTwice() {
        val input = "input"
        val hash1 = HashedPassword.hash(input)
        val hash2 = HashedPassword.hash(input)

        Assertions.assertNotEquals(hash1, hash2)
    }

    @Test
    fun checkHash() {
        val input = "input"
        val hash = HashedPassword.hash(input)

        Assertions.assertTrue(hash.check(input))
    }

    @TestFactory
    fun checkHashWrong(): List<DynamicTest> {
        val hash = HashedPassword.hash("input")
        return listOf(
                "Input",
                "INPUT",
                "inpu",
                "inputt",
                " input",
                "input ",
                " input ",
                ""
        ).map { input ->
            DynamicTest.dynamicTest("Checking password: $input") {
                Assertions.assertFalse(hash.check(input))
            }
        }
    }
}
