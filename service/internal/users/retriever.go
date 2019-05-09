package users

// UserRetriever represents a mechanism to load users from the data store
type UserRetriever interface {
	// GetUserByID will load the user with the given ID
	GetUserByID(id UserID) (UserModel, error)
}
