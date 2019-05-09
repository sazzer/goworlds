package users

import "fmt"

// UserNotFoundError represents when a user is being loaded by ID but doesn't exist
type UserNotFoundError struct {
	UserID UserID
}

// Error builds the error string
func (e UserNotFoundError) Error() string {
	return fmt.Sprintf("User not found: %s", e.UserID)
}
