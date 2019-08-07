package uk.co.grahamcox.goworlds.service.worlds

import uk.co.grahamcox.goworlds.service.model.Updater

/**
 * Interface describing how to update a world
 */
interface WorldUpdater : Updater<WorldId, WorldData>