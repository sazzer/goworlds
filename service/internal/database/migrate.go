package database

import (
	migrate "github.com/rubenv/sql-migrate"
	"github.com/sirupsen/logrus"
)

// Migrate will cause the database to be updated to the latest schema
func (db Wrapper) Migrate() error {
	migrations := &migrate.FileMigrationSource{
		Dir: "migrations",
	}

	n, err := migrate.Exec(db.db, "postgres", migrations, migrate.Up)
	if err != nil {
		logrus.WithError(err).Error("Failed to migrate database")
		return err
	}
	logrus.WithField("migrations", n).Info("Migrated database")

	return nil
}
