package users

import "time"

// UserID represents the ID of a User
type UserID string

// UserIdentity represents the identity of a User
type UserIdentity struct {
	ID      UserID
	Version string
	Created time.Time
	Updated time.Time
}

// UserData represents the data that makes up a User
type UserData struct {
	Name     string
	Email    string
	Password string
}

// UserModel represents a saved user that has been retrieved from the data store
// The difference between UserModel and UserData is that UserModel contains the identity details as well
type UserModel struct {
	UserIdentity
	UserData
}
