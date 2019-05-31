package uk.co.grahamcox.goworlds.service.model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

/**
 * Unit tests for the Page class
 */
internal class PageTest {
    @Test
    fun emptyPage() {
        val page = Page(0, 0, emptyList<Int>())

        Assertions.assertAll(
                Executable { Assertions.assertFalse(page.hasNext) },
                Executable { Assertions.assertFalse(page.hasPrevious) },
                Executable { Assertions.assertTrue(page.isFirstPage) },
                Executable { Assertions.assertTrue(page.isLastPage) }
        )
    }

    @Test
    fun onlyPage() {
        val page = Page(0, 5, listOf(0, 1, 2, 3, 4))

        Assertions.assertAll(
                Executable { Assertions.assertFalse(page.hasNext) },
                Executable { Assertions.assertFalse(page.hasPrevious) },
                Executable { Assertions.assertTrue(page.isFirstPage) },
                Executable { Assertions.assertTrue(page.isLastPage) }
        )
    }

    @Test
    fun firstPage() {
        val page = Page(0, 20, listOf(0, 1, 2, 3, 4))

        Assertions.assertAll(
                Executable { Assertions.assertTrue(page.hasNext) },
                Executable { Assertions.assertFalse(page.hasPrevious) },
                Executable { Assertions.assertTrue(page.isFirstPage) },
                Executable { Assertions.assertFalse(page.isLastPage) }
        )
    }

    @Test
    fun middlePage() {
        val page = Page(5, 20, listOf(5, 6, 7, 8, 9))

        Assertions.assertAll(
                Executable { Assertions.assertTrue(page.hasNext) },
                Executable { Assertions.assertTrue(page.hasPrevious) },
                Executable { Assertions.assertFalse(page.isFirstPage) },
                Executable { Assertions.assertFalse(page.isLastPage) }
        )
    }

    @Test
    fun lastPage() {
        val page = Page(15, 20, listOf(15, 16, 17, 18, 19))

        Assertions.assertAll(
                Executable { Assertions.assertFalse(page.hasNext) },
                Executable { Assertions.assertTrue(page.hasPrevious) },
                Executable { Assertions.assertFalse(page.isFirstPage) },
                Executable { Assertions.assertTrue(page.isLastPage) }
        )
    }

    @Test
    fun pastLastPage() {
        val page = Page(25, 20, emptyList<Int>())

        Assertions.assertAll(
                Executable { Assertions.assertFalse(page.hasNext) },
                Executable { Assertions.assertTrue(page.hasPrevious) },
                Executable { Assertions.assertFalse(page.isFirstPage) },
                Executable { Assertions.assertTrue(page.isLastPage) }
        )
    }
}
