package database

import (
	"database/sql"

	"github.com/sirupsen/logrus"
)

// Database represents a wrapper around the Database connection
type Database interface{}

// Wrapper represents a wrapper around the Database connection
type Wrapper struct {
	db *sql.DB
}

// NewFromURL creates a new Database wrapper from the given URL
func NewFromURL(url string) (Wrapper, error) {
	db, err := sql.Open("postgres", url)
	if err != nil {
		logrus.WithField("url", url).WithError(err).Error("Failed to open database connection")
		return Wrapper{}, err
	}

	logrus.WithField("url", url).Info("Opened database connection")

	return Wrapper{db}, nil
}
