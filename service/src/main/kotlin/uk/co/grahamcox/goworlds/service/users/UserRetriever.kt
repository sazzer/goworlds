package uk.co.grahamcox.goworlds.service.users

import uk.co.grahamcox.goworlds.service.model.Model

/**
 * Interface describing how to retrieve users
 */
interface UserRetriever {
    /**
     * Get the user with the given ID
     * @param id The ID of the user
     * @return the user details
     */
    fun getUserById(id : UserId) : Model<UserId, UserData>
}
