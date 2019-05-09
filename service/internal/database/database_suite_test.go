package database_test

import (
	"database/sql"
	"testing"

	"github.com/DATA-DOG/go-sqlmock"
	"github.com/stretchr/testify/suite"

	"github.com/sazzer/goworlds/service/internal/database"
)

type DatabaseSuite struct {
	suite.Suite
	testSubject database.Wrapper

	db   *sql.DB
	mock sqlmock.Sqlmock
}

func (s *DatabaseSuite) SetupTest() {
	db, mock, err := sqlmock.New(sqlmock.QueryMatcherOption(sqlmock.QueryMatcherEqual))
	s.Assert().NoError(err)

	s.db = db
	s.mock = mock

	s.testSubject = database.NewFromConnection(db)
}

func (s *DatabaseSuite) TearDownTest() {
	s.Assert().NoError(s.mock.ExpectationsWereMet())
	s.db.Close()
}

func TestDatabase(t *testing.T) {
	suite.Run(t, new(DatabaseSuite))
}
