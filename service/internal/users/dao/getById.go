package dao

import (
	"github.com/sazzer/goworlds/service/internal/database"
	"github.com/sirupsen/logrus"

	"github.com/jmoiron/sqlx"
	"github.com/sazzer/goworlds/service/internal/users"
)

// GetUserByID will load the user with the given ID
func (dao UserDAO) GetUserByID(id users.UserID) (users.UserModel, error) {
	var result users.UserModel

	err := dao.db.QueryOne("SELECT * FROM users WHERE user_id = $1", []interface{}{id}, func(row *sqlx.Rows) error {
		var user userRow
		err := row.StructScan(&user)
		if err != nil {
			return err
		}

		result = user.toUserModel()
		return nil
	})

	if err != nil {
		switch err.(type) {
		case database.NoRowsReturnedError:
			logrus.WithField("id", id).Warn("No user found for ID")
			return users.UserModel{}, users.UserNotFoundError{UserID: id}
		default:
			logrus.WithError(err).WithField("id", id).Warn("Unexpected error searching users by ID")
			return users.UserModel{}, err
		}
	}

	return result, nil
}
