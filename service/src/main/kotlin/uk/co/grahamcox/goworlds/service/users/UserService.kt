package uk.co.grahamcox.goworlds.service.users

import uk.co.grahamcox.goworlds.service.users.dao.UserCreator

/**
 * Interface for working with Users in general
 */
interface UserService : UserRetriever, UserCreator
