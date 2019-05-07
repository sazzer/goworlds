package testutils

import (
	"bytes"
	"net/http"
	"testing"

	"github.com/kinbiko/jsonassert"
)

// AssertJSONResponse will ensure that the given HTTP Response has a JSON Payload that matches that provided
func AssertJSONResponse(t *testing.T, expected string, response *http.Response) {
	ja := jsonassert.New(t)

	buf := new(bytes.Buffer)
	_, _ = buf.ReadFrom(response.Body)

	ja.Assertf(expected, buf.String())
}
