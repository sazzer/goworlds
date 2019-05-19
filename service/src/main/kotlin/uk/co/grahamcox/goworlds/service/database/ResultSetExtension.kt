package uk.co.grahamcox.goworlds.service.database

import java.sql.ResultSet
import java.util.*
import kotlin.reflect.KClass

/**
 * Get a UUID Column from the ResultSet
 * @param name The name of the column
 * @return the UUID value
 */
fun ResultSet.getUUID(name: String) = UUID.fromString(this.getString(name))

/**
 * Get an Instant Column from the ResultSet
 * @param name The name of the column
 * @return the Instant value
 */
fun ResultSet.getInstant(name: String) = this.getTimestamp(name).toInstant()

/**
 * Get an Array column from the ResultSet
 * @param name The name of the column
 * @param type The type of the array entries
 * @return the array
 */
fun <T> ResultSet.getList(name: String): List<T> = (this.getArray(name).array as Array<T>).asList()
