package password

import (
	"github.com/alexedwards/argon2id"
	"github.com/sirupsen/logrus"
)

// MismatchError represents the case when a plaintext password didn't match the hash
type MismatchError struct{}

// Error generates the error string for the MismatchError case
func (e MismatchError) Error() string {
	return "The provided passwords didn't match"
}

// Check will compare the provided hash and plaintext to see if they represent the plaintext.
// On a successful match, the return value is "nil". On a failed match - either because the passwords
// don't match or because some error occurred in the password check process
func Check(hash string, plaintext string) error {
	match, err := argon2id.ComparePasswordAndHash(plaintext, hash)

	if err != nil {
		logrus.WithError(err).Error("Failed to compare passwords")
		return err
	}

	if !match {
		return MismatchError{}
	}

	return nil
}
