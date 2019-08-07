package uk.co.grahamcox.goworlds.service.worlds

import uk.co.grahamcox.goworlds.service.model.Creator

/**
 * Interface describing how to create a world
 */
interface WorldCreator : Creator<WorldId, WorldData>