package dao_test

import (
	"database/sql"
	"testing"

	"github.com/DATA-DOG/go-sqlmock"
	"github.com/stretchr/testify/suite"

	"github.com/sazzer/goworlds/service/internal/database"
	"github.com/sazzer/goworlds/service/internal/users/dao"
)

type UserDAOSuite struct {
	suite.Suite
	testSubject dao.UserDAO

	db   *sql.DB
	mock sqlmock.Sqlmock
}

func (s *UserDAOSuite) SetupTest() {
	db, mock, err := sqlmock.New(sqlmock.QueryMatcherOption(sqlmock.QueryMatcherEqual))
	s.Assert().NoError(err)

	s.db = db
	s.mock = mock

	s.testSubject = dao.NewUserDAO(database.NewFromConnection(db))
}

func (s *UserDAOSuite) TearDownTest() {
	s.Assert().NoError(s.mock.ExpectationsWereMet())
	s.db.Close()
}

func TestUserDAO(t *testing.T) {
	suite.Run(t, new(UserDAOSuite))
}
