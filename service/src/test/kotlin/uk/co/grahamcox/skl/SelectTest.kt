package uk.co.grahamcox.skl

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.function.Executable

internal class SelectTest {
    @TestFactory
    fun test(): List<DynamicTest> {
        data class Test(
                val name: String,
                val builder: SelectBuilder.() -> Unit,
                val expectedSql: String,
                val expectedBinds: Map<String, Any?> = emptyMap()
        )

        val tests = listOf(
                Test(
                        name = "Single Table",
                        builder = {
                        },
                        expectedSql = "SELECT *"
                ),

                // Specifying tables
                Test(
                        name = "Single Table",
                        builder = {
                            from("users")
                        },
                        expectedSql = "SELECT * FROM users"
                ),
                Test(
                        name = "Multiple Tables - Same Statement",
                        builder = {
                            from("users", "clients")
                        },
                        expectedSql = "SELECT * FROM users, clients"
                ),
                Test(
                        name = "Multiple Tables - Multiple Statements",
                        builder = {
                            from("users")
                            from("clients")
                        },
                        expectedSql = "SELECT * FROM users, clients"
                ),
                Test(
                        name = "Table with Alias - Pair",
                        builder = {
                            from("users" to "u")
                        },
                        expectedSql = "SELECT * FROM users AS u"
                ),
                Test(
                        name = "Table with Alias - Rich Object",
                        builder = {
                            from(Table("users", "u"))
                        },
                        expectedSql = "SELECT * FROM users AS u"
                ),

                // Specifying Select Clauses
                Test(
                        name = "Simple Field",
                        builder = {
                            from("users")
                            selecting(Field("name"))
                        },
                        expectedSql = "SELECT name FROM users"
                ),
                Test(
                        name = "Field from Table",
                        builder = {
                            val users = Table("users")
                            from(users)
                            selecting(users.field("name"))
                        },
                        expectedSql = "SELECT users.name FROM users"
                ),
                Test(
                        name = "Field from Table with Alias",
                        builder = {
                            val users = Table("users", "u")
                            from(users)
                            selecting(users.field("name"))
                        },
                        expectedSql = "SELECT u.name FROM users AS u"
                ),
                Test(
                        name = "Multiple Field - Inline",
                        builder = {
                            from("users")
                            selecting(Field("name"), Field("age"))
                        },
                        expectedSql = "SELECT name, age FROM users"
                ),
                Test(
                        name = "Multiple Field - Separate",
                        builder = {
                            from("users")
                            selecting(Field("name"))
                            selecting(Field("age"))
                        },
                        expectedSql = "SELECT name, age FROM users"
                ),
                Test(
                        name = "Constant - String",
                        builder = {
                            from("users")
                            selecting(Constant("name"))
                        },
                        expectedSql = "SELECT 'name' FROM users"
                ),
                Test(
                        name = "Constant - Integer",
                        builder = {
                            from("users")
                            selecting(Constant(37))
                        },
                        expectedSql = "SELECT 37 FROM users"
                ),
                Test(
                        name = "Constant - Boolean True",
                        builder = {
                            from("users")
                            selecting(Constant(true))
                        },
                        expectedSql = "SELECT TRUE FROM users"
                ),
                Test(
                        name = "Constant - Boolean False",
                        builder = {
                            from("users")
                            selecting(Constant(false))
                        },
                        expectedSql = "SELECT FALSE FROM users"
                ),
                Test(
                        name = "Constant - Null",
                        builder = {
                            from("users")
                            selecting(Constant(null))
                        },
                        expectedSql = "SELECT NULL FROM users"
                ),
                Test(
                        name = "Field with Upper Function",
                        builder = {
                            from("users")
                            selecting(upper(Field("name")))
                        },
                        expectedSql = "SELECT UPPER(name) FROM users"
                ),
                Test(
                        name = "Field with Alias",
                        builder = {
                            from("users")
                            selecting(alias(Field("name"), "n"))
                        },
                        expectedSql = "SELECT name AS n FROM users"
                ),

                // Specifying Limit and Offset
                Test(
                        name = "With Limit",
                        builder = {
                            from("users")
                            limit(10)
                        },
                        expectedSql = "SELECT * FROM users LIMIT 10"
                ),
                Test(
                        name = "With Offset",
                        builder = {
                            from("users")
                            offset(10)
                        },
                        expectedSql = "SELECT * FROM users OFFSET 10"
                ),
                Test(
                        name = "With Limit and Offset",
                        builder = {
                            from("users")
                            limit(10)
                            offset(20)
                        },
                        expectedSql = "SELECT * FROM users LIMIT 10 OFFSET 20"
                ),

                // Specifying Order By
                Test(
                        name = "With Single Order By clause",
                        builder = {
                            from("users")
                            orderBy(Field("name"))
                        },
                        expectedSql = "SELECT * FROM users ORDER BY name ASC"
                ),
                Test(
                        name = "With Single Order By clause in Descending order",
                        builder = {
                            from("users")
                            orderBy(Field("name"), SortOrder.DESCENDING)
                        },
                        expectedSql = "SELECT * FROM users ORDER BY name DESC"
                ),
                Test(
                        name = "With Multiple Order By clauses - repeated",
                        builder = {
                            from("users")
                            orderBy(Field("name"))
                            orderBy(Field("age"))
                        },
                        expectedSql = "SELECT * FROM users ORDER BY name ASC, age ASC"
                ),
                Test(
                        name = "With Multiple Order By clauses - repeated and mixed direction",
                        builder = {
                            from("users")
                            orderBy(Field("name"))
                            orderBy(Field("age"), SortOrder.DESCENDING)
                        },
                        expectedSql = "SELECT * FROM users ORDER BY name ASC, age DESC"
                ),
                Test(
                        name = "With Multiple Order By clauses - Combined",
                        builder = {
                            from("users")
                            orderBy(OrderBy(Field("name")), OrderBy(Field("age")))
                        },
                        expectedSql = "SELECT * FROM users ORDER BY name ASC, age ASC"
                ),

                // Specifying Where
                Test(
                        name = "With simple Condition",
                        builder = {
                            from("users")
                            where(BiConditional(Field("name"), "=", Constant("Graham")))
                        },
                        expectedSql = "SELECT * FROM users WHERE name = 'Graham'"
                ),
                Test(
                        name = "With two Conditions - separate",
                        builder = {
                            from("users")
                            where(BiConditional(Field("name"), "=", Constant("Graham")))
                            where(BiConditional(Field("age"), ">", Constant(30)))
                        },
                        expectedSql = "SELECT * FROM users WHERE name = 'Graham' AND age > 30"
                ),
                Test(
                        name = "With two Conditions - combined",
                        builder = {
                            from("users")
                            where(
                                    BiConditional(Field("name"), "=", Constant("Graham")),
                                    BiConditional(Field("age"), ">", Constant(30))
                            )
                        },
                        expectedSql = "SELECT * FROM users WHERE name = 'Graham' AND age > 30"
                ),
                Test(
                        name = "With Lambda-based Condition - Equals",
                        builder = {
                            from("users")
                            where {
                                eq(Field("name"), Constant("Graham"))
                            }
                        },
                        expectedSql = "SELECT * FROM users WHERE (name = 'Graham')"
                ),
                Test(
                        name = "With Lambda-based Condition - Greater Than",
                        builder = {
                            from("users")
                            where {
                                gt(Field("age"), Constant(30))
                            }
                        },
                        expectedSql = "SELECT * FROM users WHERE (age > 30)"
                ),
                Test(
                        name = "With Lambda-based Condition - Less Than",
                        builder = {
                            from("users")
                            where {
                                lt(Field("age"), Constant(30))
                            }
                        },
                        expectedSql = "SELECT * FROM users WHERE (age < 30)"
                ),
                Test(
                        name = "With Lambda-based Condition - Greater Than Or Equals",
                        builder = {
                            from("users")
                            where {
                                gte(Field("age"), Constant(30))
                            }
                        },
                        expectedSql = "SELECT * FROM users WHERE (age >= 30)"
                ),
                Test(
                        name = "With Lambda-based Condition - Less Than Or Equals",
                        builder = {
                            from("users")
                            where {
                                lte(Field("age"), Constant(30))
                            }
                        },
                        expectedSql = "SELECT * FROM users WHERE (age <= 30)"
                ),
                Test(
                        name = "With Lambda-based Condition - Multiple Conditions",
                        builder = {
                            from("users")
                            where {
                                eq(Field("name"), Constant("Graham"))
                                gt(Field("age"), Constant(30))
                            }
                        },
                        expectedSql = "SELECT * FROM users WHERE (name = 'Graham' AND age > 30)"
                ),
                Test(
                        name = "With Lambda-based Condition - No Conditions",
                        builder = {
                            from("users")
                            where {
                            }
                        },
                        expectedSql = "SELECT * FROM users"
                ),
                Test(
                        name = "With Lambda-based Condition - One populated and one empty",
                        builder = {
                            from("users")
                            where {
                                eq(Field("name"), Constant("Graham"))
                            }
                            where {
                            }
                        },
                        expectedSql = "SELECT * FROM users WHERE (name = 'Graham')"
                ),

                // Everything together
                Test(
                        name = "Everything together",
                        builder = {
                            val (users) = from("users")

                            selecting(users["name"], users["age"])

                            where {
                                eq(users["name"], bind("Graham"))
                                gt(users["age"], bind(30))
                                notNull(users["activated"])
                            }

                            orderBy(users["age"], SortOrder.ASCENDING)

                            limit(10)
                            offset(5)
                        },
                        expectedSql = "SELECT users.name, users.age FROM users WHERE (users.name = :bind0 AND users.age > :bind1 AND users.activated IS NOT NULL) ORDER BY users.age ASC LIMIT 10 OFFSET 5",
                        expectedBinds = mapOf(
                                "bind0" to "Graham",
                                "bind1" to 30
                        )
                )

        )

        return tests.map { test ->
            DynamicTest.dynamicTest(test.name) {
                val query = select(test.builder)

                Assertions.assertAll(
                        Executable { Assertions.assertEquals(test.expectedSql, query.sql) },
                        Executable { Assertions.assertEquals(test.expectedBinds, query.binds) }
                )
            }
        }
    }

    @Test
    fun kitchenSink() {
        val query = select {
            val (users) = from("users")

            selecting(users["name"], users["age"])

            where {
                eq(users["name"], bind("sazzer"))
                gt(users["age"], bind(30))
                notNull(users["activated"])
            }

            orderBy(users["age"], SortOrder.ASCENDING)

            limit(10)
            offset(5)
        }

        Assertions.assertAll(
                Executable { Assertions.assertEquals("SELECT users.name, users.age FROM users WHERE (users.name = :bind0 AND users.age > :bind1 AND users.activated IS NOT NULL) ORDER BY users.age ASC LIMIT 10 OFFSET 5",
                        query.sql) },
                Executable { Assertions.assertEquals(mapOf(
                        "bind0" to "sazzer",
                        "bind1" to 30
                ), query.binds) }
        )
    }
}
