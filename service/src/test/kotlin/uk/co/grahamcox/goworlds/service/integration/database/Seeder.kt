package uk.co.grahamcox.goworlds.service.integration.database

import java.time.Instant
import java.util.*

/**
 * Base class to allow seeding of data
 */
interface Seeder {
    /** The SQL to seed with */
    val seedSql: String

    /** The params to seed with */
    val seedParams: Map<String, Any?>
        get() = this.javaClass.declaredFields.map {
            it.isAccessible = true
            val value = it.get(this)

            val realValue = if (value is Instant) {
                Date.from(value)
            } else {
                value
            }

            it.name to realValue
        }.toMap()
}
