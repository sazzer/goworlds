package database

// CheckHealth performs a healthcheck against the database connection
func (d Wrapper) CheckHealth() error {
	_, err := d.db.Exec("SELECT 1")
	return err
}
