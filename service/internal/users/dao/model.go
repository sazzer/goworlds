package dao

import (
	"time"

	"github.com/sazzer/goworlds/service/internal/users"
)

type userRow struct {
	UserID   string    `db:"user_id"`
	Version  string    `db:"version"`
	Created  time.Time `db:"created"`
	Updated  time.Time `db:"updated"`
	Name     string    `db:"name"`
	Email    string    `db:"email"`
	Password string    `db:"password"`
}

func (row userRow) toUserModel() users.UserModel {
	return users.UserModel{
		UserIdentity: users.UserIdentity{
			ID:      users.UserID(row.UserID),
			Version: row.Version,
			Created: row.Created,
			Updated: row.Updated,
		},
		UserData: users.UserData{
			Name:     row.Name,
			Email:    row.Email,
			Password: row.Password,
		},
	}
}
