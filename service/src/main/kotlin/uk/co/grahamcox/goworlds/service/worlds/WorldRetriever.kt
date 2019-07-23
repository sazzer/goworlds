package uk.co.grahamcox.goworlds.service.worlds

import uk.co.grahamcox.goworlds.service.model.Retriever

/**
 * Interface describing how to retrieve worlds
 */
interface WorldRetriever : Retriever<WorldId, WorldData>