package uk.co.grahamcox.goworlds.service.users

import uk.co.grahamcox.goworlds.service.model.Updater

/**
 * Interface describing how to update a user
 */
interface UserUpdater : Updater<UserId, UserData>
