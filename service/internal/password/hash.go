package password

import (
	"github.com/alexedwards/argon2id"
	"github.com/sirupsen/logrus"
)

// hashParams represents the parameters to the password hash algorithm
var hashParams = argon2id.Params{
	Memory:      64 * 1024,
	Iterations:  3,
	Parallelism: 2,
	SaltLength:  16,
	KeyLength:   32,
}

// Hash will convert a plaintext string into a securely hashed string
func Hash(plaintext string) (string, error) {
	hash, err := argon2id.CreateHash(plaintext, &hashParams)

	if err != nil {
		logrus.WithError(err).Error("Failed to hash password")
		return "", err
	}

	return hash, nil
}
