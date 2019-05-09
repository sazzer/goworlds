package dao_test

import (
	"time"

	"github.com/DATA-DOG/go-sqlmock"
	"github.com/sazzer/goworlds/service/internal/users"
)

func (s *UserDAOSuite) TestGetUnknownUserByID() {
	rows := sqlmock.NewRows([]string{"user_id", "version", "created", "updated", "name", "email", "password"})
	s.mock.ExpectQuery("SELECT * FROM users WHERE user_id = $1").
		WithArgs("unknown").
		WillReturnRows(rows)

	user, err := s.testSubject.GetUserByID("unknown")

	s.Assert().Equal(users.UserModel{}, user)
	s.Assert().Error(err)
	s.Assert().Equal(users.UserNotFoundError{UserID: "unknown"}, err)
}

func (s *UserDAOSuite) TestGetKnownUserByID() {
	created := time.Date(2019, 5, 8, 9, 39, 0, 0, time.UTC)
	updated := time.Date(2019, 5, 9, 9, 39, 0, 0, time.UTC)

	rows := sqlmock.NewRows([]string{"user_id", "version", "created", "updated", "name", "email", "password"})
	rows.AddRow("known", "version", created, updated, "Graham", "graham@grahamcox.co.uk", "hashedPassword")
	s.mock.ExpectQuery("SELECT * FROM users WHERE user_id = $1").
		WithArgs("known").
		WillReturnRows(rows)

	user, err := s.testSubject.GetUserByID("known")

	s.Assert().Equal(users.UserModel{
		UserIdentity: users.UserIdentity{
			ID:      "known",
			Version: "version",
			Created: created,
			Updated: updated,
		},
		UserData: users.UserData{
			Name:     "Graham",
			Email:    "graham@grahamcox.co.uk",
			Password: "hashedPassword",
		},
	}, user)
	s.Assert().NoError(err)
}
