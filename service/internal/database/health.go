package database

// CheckHealth performs a healthcheck against the database connection
func (d Database) CheckHealth() error {
	_, err := d.db.Exec("SELECT 1")
	return err
}
