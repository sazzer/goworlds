package database

import (
	"database/sql"

	"github.com/sirupsen/logrus"
)

// Database represents a wrapper around the Database connection
type Database struct {
	db *sql.DB
}

// NewFromURL creates a new Database wrapper from the given URL
func NewFromURL(url string) (Database, error) {
	db, err := sql.Open("postgres", url)
	if err != nil {
		logrus.WithField("url", url).WithError(err).Error("Failed to open database connection")
		return Database{}, err
	}

	logrus.WithField("url", url).Info("Opened database connection")

	return Database{db}, nil
}
