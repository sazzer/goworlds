package database_test

import (
	"errors"

	"github.com/DATA-DOG/go-sqlmock"
	"github.com/jmoiron/sqlx"
	"github.com/sazzer/goworlds/service/internal/database"
)

func (s *DatabaseSuite) TestQueryOne_NoRows() {
	rows := sqlmock.NewRows([]string{"answer"})
	s.mock.ExpectQuery("SELECT answer FROM question").
		WillReturnRows(rows)

	err := s.testSubject.QueryOne("SELECT answer FROM question", []interface{}{}, func(row *sqlx.Rows) error {
		return nil
	})

	s.Assert().Equal(database.NoRowsReturnedError{}, err)
}

func (s *DatabaseSuite) TestQueryOne_NoRows_Binds() {
	rows := sqlmock.NewRows([]string{"answer"})
	s.mock.ExpectQuery("SELECT answer FROM question").
		WithArgs("foo", "bar").
		WillReturnRows(rows)

	err := s.testSubject.QueryOne("SELECT answer FROM question", []interface{}{"foo", "bar"}, func(row *sqlx.Rows) error {
		return nil
	})

	s.Assert().Equal(database.NoRowsReturnedError{}, err)
}

func (s *DatabaseSuite) TestQueryOne_OneRow() {
	rows := sqlmock.NewRows([]string{"answer"})
	rows.AddRow(42)
	s.mock.ExpectQuery("SELECT answer FROM question").
		WillReturnRows(rows)

	type rowStruct struct {
		Answer int `sql:"answer"`
	}

	returnedRows := []rowStruct{}

	err := s.testSubject.QueryOne("SELECT answer FROM question", []interface{}{}, func(row *sqlx.Rows) error {
		var result rowStruct
		err := row.StructScan(&result)
		s.Assert().NoError(err)

		returnedRows = append(returnedRows, result)
		return nil
	})

	s.Assert().NoError(err)

	s.Assert().Equal([]rowStruct{{42}}, returnedRows)
}

func (s *DatabaseSuite) TestQueryOne_TwoRows() {
	rows := sqlmock.NewRows([]string{"answer"})
	rows.AddRow(42)
	rows.AddRow(21)
	s.mock.ExpectQuery("SELECT answer FROM question").
		WillReturnRows(rows)

	type rowStruct struct {
		Answer int `sql:"answer"`
	}

	returnedRows := []rowStruct{}

	err := s.testSubject.QueryOne("SELECT answer FROM question", []interface{}{}, func(row *sqlx.Rows) error {
		var result rowStruct
		err := row.StructScan(&result)
		s.Assert().NoError(err)

		returnedRows = append(returnedRows, result)
		return nil
	})

	s.Assert().Equal(database.MultipleRowsReturnedError{}, err)

	s.Assert().Equal([]rowStruct{{42}}, returnedRows)
}

func (s *DatabaseSuite) TestQueryOne_OneRow_Error() {
	rows := sqlmock.NewRows([]string{"answer"})
	rows.AddRow(42)
	s.mock.ExpectQuery("SELECT answer FROM question").
		WillReturnRows(rows)

	type rowStruct struct {
		Answer int `sql:"answer"`
	}

	returnedRows := []rowStruct{}

	rowError := errors.New("Oops")

	err := s.testSubject.QueryOne("SELECT answer FROM question", []interface{}{}, func(row *sqlx.Rows) error {
		var result rowStruct
		err := row.StructScan(&result)
		s.Assert().NoError(err)

		returnedRows = append(returnedRows, result)
		return rowError
	})

	s.Assert().Equal(rowError, err)

	s.Assert().Equal([]rowStruct{{42}}, returnedRows)
}
