package database

import (
	"database/sql"

	"github.com/jmoiron/sqlx"
	"github.com/sirupsen/logrus"
)

// RowCallback represents a callback function that is triggered for every row processed
type RowCallback func(rows *sqlx.Rows) error

// Database represents a wrapper around the Database connection
type Database interface {
	// QueryOne will perform a query for a single row, returning an error if either too few or too many are returned
	QueryOne(query string, binds []interface{}, callback RowCallback) error
}

// Wrapper represents a wrapper around the Database connection
type Wrapper struct {
	db *sqlx.DB
}

// NewFromConnection creates a new Database wrapper from the given already open database connection
func NewFromConnection(db *sql.DB) Wrapper {
	return Wrapper{sqlx.NewDb(db, "postgres")}
}

// NewFromURL creates a new Database wrapper from the given URL
func NewFromURL(url string) (Wrapper, error) {
	db, err := sql.Open("postgres", url)
	if err != nil {
		logrus.WithField("url", url).WithError(err).Error("Failed to open database connection")
		return Wrapper{}, err
	}

	logrus.WithField("url", url).Info("Opened database connection")

	return NewFromConnection(db), nil
}
