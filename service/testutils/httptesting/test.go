package httptesting

import (
	"net/http"
	"net/http/httptest"
	"testing"
)

// HandlerTest represents a test for an HTTP Handler
type HandlerTest struct {
	handler http.HandlerFunc
}

// NewHandlerTest creates a new test for testing the given handler
func NewHandlerTest(handler http.HandlerFunc) HandlerTest {
	return HandlerTest{handler}
}

// HandlerTestResponse represents the response from a single request into a handler under test
type HandlerTestResponse struct {
	t   *testing.T
	req *http.Request
	res *http.Response
	rec *httptest.ResponseRecorder
}

// Handle will attempt to handle the given request with the handler under test and return something to assert on the response
func (h HandlerTest) Handle(t *testing.T, req *http.Request) HandlerTestResponse {
	w := httptest.NewRecorder()
	h.handler(w, req)

	response := w.Result()

	return HandlerTestResponse{
		t:   t,
		req: req,
		res: response,
		rec: w,
	}
}
