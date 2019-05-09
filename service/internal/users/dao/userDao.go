package dao

import "github.com/sazzer/goworlds/service/internal/database"

// UserDAO represents the DAO layer to interact with users
type UserDAO struct {
	db database.Database
}

// NewUserDAO will create a new User DAO
func NewUserDAO(db database.Database) UserDAO {
	return UserDAO{db: db}
}
