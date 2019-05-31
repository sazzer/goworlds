package uk.co.grahamcox.goworlds.service.users

import uk.co.grahamcox.goworlds.service.model.Model
import uk.co.grahamcox.goworlds.service.model.Page
import uk.co.grahamcox.goworlds.service.model.Sort

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

    /**
     * Search for users in the system
     * @param filters Filters to apply when searching
     * @param sorts Sorts to apply for the returned users
     * @param offset The offset of the results to return
     * @param count The count of results to return
     * @return the matching users
     */
    fun searchUsers(filters: UserSearchFilters = UserSearchFilters(),
                    sorts: List<Sort<UserSort>> = emptyList(),
                    offset: Long = 0,
                    count: Long = 10) : Page<Model<UserId, UserData>>
}
