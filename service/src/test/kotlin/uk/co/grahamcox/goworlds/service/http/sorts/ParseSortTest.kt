package uk.co.grahamcox.goworlds.service.http.sorts

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import uk.co.grahamcox.goworlds.service.model.Sort
import uk.co.grahamcox.goworlds.service.model.SortDirection
import uk.co.grahamcox.goworlds.service.users.UserSort

internal class ParseSortTest {
    @TestFactory
    fun parseValidStrings(): List<DynamicTest> {
        val tests: List<Pair<String, List<Sort<UserSort>>>> = listOf(
                "" to emptyList(),

                "name" to listOf(Sort(UserSort.NAME, SortDirection.ASCENDING)),
                "NAME" to listOf(Sort(UserSort.NAME, SortDirection.ASCENDING)),
                "Name" to listOf(Sort(UserSort.NAME, SortDirection.ASCENDING)),
                "NaMe" to listOf(Sort(UserSort.NAME, SortDirection.ASCENDING)),

                "+name" to listOf(Sort(UserSort.NAME, SortDirection.ASCENDING)),
                "-name" to listOf(Sort(UserSort.NAME, SortDirection.DESCENDING)),

                "  name" to listOf(Sort(UserSort.NAME, SortDirection.ASCENDING)),
                "name  " to listOf(Sort(UserSort.NAME, SortDirection.ASCENDING)),
                "  name  " to listOf(Sort(UserSort.NAME, SortDirection.ASCENDING)),

                "name,created" to listOf(
                        Sort(UserSort.NAME, SortDirection.ASCENDING),
                        Sort(UserSort.CREATED, SortDirection.ASCENDING)),
                "name,  created" to listOf(
                        Sort(UserSort.NAME, SortDirection.ASCENDING),
                        Sort(UserSort.CREATED, SortDirection.ASCENDING)),
                "name  ,created" to listOf(
                        Sort(UserSort.NAME, SortDirection.ASCENDING),
                        Sort(UserSort.CREATED, SortDirection.ASCENDING)),
                "name  ,  created" to listOf(
                        Sort(UserSort.NAME, SortDirection.ASCENDING),
                        Sort(UserSort.CREATED, SortDirection.ASCENDING)),
                "+name,+created" to listOf(
                        Sort(UserSort.NAME, SortDirection.ASCENDING),
                        Sort(UserSort.CREATED, SortDirection.ASCENDING)),
                "-name,-created" to listOf(
                        Sort(UserSort.NAME, SortDirection.DESCENDING),
                        Sort(UserSort.CREATED, SortDirection.DESCENDING)),
                "+name,-created" to listOf(
                        Sort(UserSort.NAME, SortDirection.ASCENDING),
                        Sort(UserSort.CREATED, SortDirection.DESCENDING)),
                "+name,created" to listOf(
                        Sort(UserSort.NAME, SortDirection.ASCENDING),
                        Sort(UserSort.CREATED, SortDirection.ASCENDING)),
                "name,-created" to listOf(
                        Sort(UserSort.NAME, SortDirection.ASCENDING),
                        Sort(UserSort.CREATED, SortDirection.DESCENDING))
        )

        return tests.map { (input, expected) ->
            DynamicTest.dynamicTest("Input: '$input'") {
                val result = parseSorts<UserSort>(input)

                Assertions.assertEquals(expected, result)
            }
        }
    }
    @TestFactory
    fun parseInvalidStrings(): List<DynamicTest> {
        val tests: List<Pair<String, List<String>>> = listOf(
                "unknown" to listOf("unknown"),
                "name,unknown" to listOf("unknown"),
                "unknown,name" to listOf("unknown"),
                "nam e" to listOf("nam e"),
                "nam,e" to listOf("nam", "e"),
                "*name" to listOf("*name")

        )

        return tests.map { (input, expected) ->
            DynamicTest.dynamicTest("Input: '$input'") {
                val e = Assertions.assertThrows(UnknownSortKeyException::class.java) {
                    parseSorts<UserSort>(input)
                }

                Assertions.assertEquals(expected, e.unknownSorts)
            }
        }
    }
}
