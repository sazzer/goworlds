package uk.co.grahamcox.goworlds.service.users

import uk.co.grahamcox.goworlds.service.model.Model

/**
 * Interface describing how to update a user
 */
interface UserUpdater {
    /**
     * Update the user to the given data
     * @param userId The ID of the user
     * @param data The new data
     * @return the newly updated user
     */
    fun updateUser(userId: UserId, data: UserData) : Model<UserId, UserData>

    /**
     * Update the user with the given lambda
     * @param userId The ID of the user
     * @param modifier The means to mutate the user
     * @return the newly updated user
     */
    fun updateUser(userId: UserId, modifier: (Model<UserId, UserData>) -> UserData) : Model<UserId, UserData>
}
