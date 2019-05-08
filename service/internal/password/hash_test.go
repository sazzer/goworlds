package password_test

import (
	"testing"
	"testing/quick"

	"github.com/stretchr/testify/assert"

	"github.com/sazzer/goworlds/service/internal/password"
)

func TestHash(t *testing.T) {
	f := func(input string) bool {
		hash, err := password.Hash(input)
		assert.NoError(t, err)
		assert.NotEqual(t, "", hash)

		return err == nil && hash != ""
	}

	if err := quick.Check(f, nil); err != nil {
		t.Error(err)
	}
}

func TestReHash(t *testing.T) {
	f := func(input string) bool {
		hash1, err := password.Hash(input)
		assert.NoError(t, err)

		hash2, err := password.Hash(input)
		assert.NoError(t, err)

		assert.NotEqual(t, hash1, hash2)

		return err == nil && hash1 != hash2
	}

	if err := quick.Check(f, nil); err != nil {
		t.Error(err)
	}
}
