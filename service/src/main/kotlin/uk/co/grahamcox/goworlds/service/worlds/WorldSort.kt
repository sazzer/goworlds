package uk.co.grahamcox.goworlds.service.worlds

/**
 * Enumeration of fields that a list of worlds can be sorted by
 */
enum class WorldSort {
    /** Sort by the worlds name */
    NAME,

    /** Sort by the created date */
    CREATED,

    /** Sort by the last updated date */
    UPDATED,

    /** Sort by the name of the owner */
    OWNER
}