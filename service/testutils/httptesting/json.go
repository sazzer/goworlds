package httptesting

import (
	"bytes"

	"github.com/kinbiko/jsonassert"
)

// IsStatus tests that the status code in the response is the one expected
func (h HandlerTestResponse) IsJSONBody(body string) HandlerTestResponse {
	ja := jsonassert.New(h.t)

	buf := new(bytes.Buffer)
	_, _ = buf.ReadFrom(h.res.Body)

	ja.Assertf(body, buf.String())
	return h
}
