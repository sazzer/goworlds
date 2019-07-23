package uk.co.grahamcox.goworlds.service.worlds

import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.model.Retriever

/**
 * Interface describing how to retrieve worlds
 */
interface WorldRetriever : Retriever<WorldId, WorldData> {
    /**
     * Get the world with the given URL Slug
     * @param slug The URL Slug of the World
     * @return the World details
     */
    fun getBySlug(slug: String) : Model<WorldId, WorldData>
}
