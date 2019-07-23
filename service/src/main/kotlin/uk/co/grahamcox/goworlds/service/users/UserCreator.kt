package uk.co.grahamcox.goworlds.service.users

import uk.co.grahamcox.goworlds.service.model.Creator

/**
 * Interface describing how to create a user
 */
interface UserCreator : Creator<UserId, UserData>