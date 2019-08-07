package uk.co.grahamcox.goworlds.service.users

import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.model.Retriever
import uk.co.grahamcox.goworlds.service.model.Searcher

/**
 * Interface describing how to retrieve users
 */
interface UserRetriever : Retriever<UserId, UserData>, Searcher<UserId, UserData, UserSearchFilters, UserSort> {
    /**
     * Get the user with the given Email
     * @param email The email of the user
     * @return the user details
     */
    fun getByEmail(email: String) : Model<UserId, UserData>
}
