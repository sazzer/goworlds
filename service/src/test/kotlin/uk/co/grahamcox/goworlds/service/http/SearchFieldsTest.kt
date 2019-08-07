package uk.co.grahamcox.goworlds.service.http

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import uk.co.grahamcox.goworlds.service.http.problems.InvalidCountException
import uk.co.grahamcox.goworlds.service.http.problems.InvalidOffsetException
import uk.co.grahamcox.goworlds.service.http.sorts.UnknownSortKeyException
import uk.co.grahamcox.goworlds.service.model.Sort
import uk.co.grahamcox.goworlds.service.model.SortDirection
import uk.co.grahamcox.goworlds.service.users.UserSort

internal class SearchFieldsTest {
    @TestFactory
    fun testParseSuccess(): List<DynamicTest> {
        data class Test(
                val offset: String? = null,
                val count: String? = null,
                val sort: String? = null,
                val result: SearchFields<UserSort>
        )

        return listOf(
                Test(result = SearchFields(0, 10, emptyList())),
                Test(offset = "0", result = SearchFields(0, 10, emptyList())),
                Test(offset = "10", result = SearchFields(10, 10, emptyList())),
                Test(count = "10", result = SearchFields(0, 10, emptyList())),
                Test(count = "20", result = SearchFields(0, 20, emptyList())),
                Test(sort = "name", result = SearchFields(0, 10, listOf(Sort(UserSort.NAME, SortDirection.ASCENDING))))
        ).map { test ->
            DynamicTest.dynamicTest(test.toString()) {
                val result = parseSearchFields<UserSort>(test.offset, test.count, test.sort)

                Assertions.assertEquals(test.result, result)
            }
        }
    }

    @TestFactory
    fun testParseFailure(): List<DynamicTest> {
        data class Test(
                val offset: String? = null,
                val count: String? = null,
                val sort: String? = null,
                val result: Exception
        )

        return listOf(
                Test(offset = "a", result = InvalidOffsetException()),
                Test(offset = "-1", result = InvalidOffsetException()),
                Test(offset = "1.0", result = InvalidOffsetException()),
                Test(count = "a", result = InvalidCountException()),
                Test(count = "-1", result = InvalidCountException()),
                Test(count = "1.0", result = InvalidCountException()),
                Test(sort = "unknown", result = UnknownSortKeyException(listOf("unknown")))
        ).map { test ->
            DynamicTest.dynamicTest(test.toString()) {
                Assertions.assertThrows(test.result.javaClass) {
                    parseSearchFields<UserSort>(test.offset, test.count, test.sort)
                }
            }
        }
    }
}