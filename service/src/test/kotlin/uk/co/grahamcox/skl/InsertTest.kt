package uk.co.grahamcox.skl

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.function.Executable

internal class InsertTest {
    @TestFactory
    fun test(): List<DynamicTest> {
        data class Test(
                val name: String,
                val table: String,
                val builder: InsertBuilder.() -> Unit,
                val expectedSql: String,
                val expectedBinds: Map<String, Any?> = emptyMap()
        )

        val tests = listOf(
                Test(
                        name = "Constant Value",
                        table = "users",
                        builder = {
                            set("name", Constant("Graham"))
                        },
                        expectedSql = "INSERT INTO users(name) VALUES ('Graham')"
                ),
                Test(
                        name = "Bind Value",
                        table = "users",
                        builder = {
                            set("name", bind("Graham"))
                        },
                        expectedSql = "INSERT INTO users(name) VALUES (:bind0)",
                        expectedBinds = mapOf(
                                "bind0" to "Graham"
                        )
                ),
                Test(
                        name = "Returning Row",
                        table = "users",
                        builder = {
                            set("name", bind("Graham"))
                            returnAll()
                        },
                        expectedSql = "INSERT INTO users(name) VALUES (:bind0) RETURNING *",
                        expectedBinds = mapOf(
                                "bind0" to "Graham"
                        )
                ),
                Test(
                        name = "Returning Field",
                        table = "users",
                        builder = {
                            set("name", bind("Graham"))
                            returning("user_id")
                        },
                        expectedSql = "INSERT INTO users(name) VALUES (:bind0) RETURNING users.user_id",
                        expectedBinds = mapOf(
                                "bind0" to "Graham"
                        )
                )
        )

        return tests.map { test ->
            DynamicTest.dynamicTest(test.name) {
                val query = insert(test.table, test.builder)

                Assertions.assertAll(
                        Executable { Assertions.assertEquals(test.expectedSql, query.sql) },
                        Executable { Assertions.assertEquals(test.expectedBinds, query.binds) }
                )
            }
        }
    }
}
