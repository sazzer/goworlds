package database

// NoRowsReturnedError means that we expected exactly one row but didn't get any
type NoRowsReturnedError struct{}

func (e NoRowsReturnedError) Error() string {
	return "Expected exactly one row, but got 0"
}

// MultipleRowsReturnedError means that we expected exactly one row but got 2 or more
type MultipleRowsReturnedError struct{}

func (e MultipleRowsReturnedError) Error() string {
	return "Expected exactly one row, but got 2+"
}

// QueryOne will perform a query for a single row, returning an error if either too few or too many are returned
func (db Wrapper) QueryOne(query string, binds []interface{}, callback RowCallback) error {
	// Actually execute the query
	rows, err := db.db.Queryx(query, binds...)
	if err != nil {
		return err
	}
	// Ensure that the resultset is closed afterwards
	defer rows.Close()

	// If we got no rows back then this is an error
	if !rows.Next() {
		return NoRowsReturnedError{}
	}

	// Process the first row
	if err = callback(rows); err != nil {
		return err
	}

	// If we got a second row back then this is also an error
	if rows.Next() {
		return MultipleRowsReturnedError{}
	}

	return nil
}
