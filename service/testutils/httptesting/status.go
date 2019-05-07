package httptesting

import "github.com/stretchr/testify/assert"

// IsStatus tests that the status code in the response is the one expected
func (h HandlerTestResponse) IsStatus(status int) HandlerTestResponse {
	assert.Equal(h.t, status, h.res.StatusCode)
	return h
}
