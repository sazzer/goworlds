package server_test

import (
	"bytes"
	"net/http/httptest"
	"testing"

	"github.com/kinbiko/jsonassert"
	"github.com/sazzer/goworlds/service/internal/server"
	"github.com/stretchr/testify/assert"
)

type TestStruct struct {
	Foo    string `json:"foo"`
	Answer int    `json:"answer"`
}

func TestJSONResponse(t *testing.T) {
	testCases := []struct {
		name   string
		input  interface{}
		output string
	}{
		{"string", "Hello", `"Hello"`},
		{"array of string", []string{"a", "b", "c"}, `["a", "b", "c"]`},
		{"array of int", []int{1, 2, 3}, `[1, 2, 3]`},
		{"mixed array", []interface{}{"abc", 123}, `["abc", 123]`},
		{"map", map[string]interface{}{"foo": "bar", "answer": 42}, `{"foo": "bar", "answer": 42}`},
		{"struct", TestStruct{"bar", 42}, `{"foo": "bar", "answer": 42}`},
	}

	for _, tc := range testCases {
		t.Run(tc.name, func(t *testing.T) {
			w := httptest.NewRecorder()
			server.JSONResponse{
				Body:       tc.input,
				StatusCode: 200,
			}.Write(w)

			response := w.Result()

			assert.Equal(t, 200, response.StatusCode)

			ja := jsonassert.New(t)

			buf := new(bytes.Buffer)
			buf.ReadFrom(response.Body)

			ja.Assertf(tc.output, buf.String())
		})
	}
}

func TestStatusCode(t *testing.T) {
	testCases := []struct {
		name       string
		statusCode int
		expected   int
	}{
		{"Not Set", 0, 200},
		{"OK", 200, 200},
		{"Redirect", 304, 304},
		{"Not Found", 404, 404},
		{"Server Error", 500, 500},
		{"Unknown Error", 599, 599},
	}

	for _, tc := range testCases {
		t.Run(tc.name, func(t *testing.T) {
			w := httptest.NewRecorder()
			server.JSONResponse{
				Body:       "Hello",
				StatusCode: tc.statusCode,
			}.Write(w)

			response := w.Result()

			assert.Equal(t, tc.expected, response.StatusCode)

			ja := jsonassert.New(t)

			buf := new(bytes.Buffer)
			buf.ReadFrom(response.Body)

			ja.Assertf(`"Hello"`, buf.String())
		})
	}
}
