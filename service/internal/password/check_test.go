package password_test

import (
	"testing"

	"github.com/stretchr/testify/assert"

	"github.com/sazzer/goworlds/service/internal/password"
)

func TestCheck(t *testing.T) {
	input := "password"

	hash, _ := password.Hash(input)

	err := password.Check(hash, input)
	assert.NoError(t, err)
}

func TestCheckWrong(t *testing.T) {
	hash, _ := password.Hash("password")

	err := password.Check(hash, "other")
	assert.Error(t, err)
}
