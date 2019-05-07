package handlers_test

import (
	"errors"
	"net/http/httptest"
	"testing"

	"github.com/sazzer/goworlds/service/internal/health"
	"github.com/sazzer/goworlds/service/internal/health/handlers"
	"github.com/sazzer/goworlds/service/testutils"
	"github.com/stretchr/testify/assert"
)

type TestHealthcheck struct {
	response error
}

func (t TestHealthcheck) CheckHealth() error {
	return t.response
}

func TestHealthchecks(t *testing.T) {
	testCases := []struct {
		name       string
		checks     map[string]health.Healthcheck
		statusCode int
		output     string
	}{
		{"No checks", map[string]health.Healthcheck{}, 200, `{"status": "SUCCESS", "details": {}}`},
		{
			name: "Passing Check",
			checks: map[string]health.Healthcheck{
				"passing": TestHealthcheck{},
			},
			statusCode: 200,
			output: `{
				"status": "SUCCESS",
				"details": {
					"passing": {
						"status": "SUCCESS"
					}
				}
			}`,
		},
		{
			name: "Failing Check",
			checks: map[string]health.Healthcheck{
				"failing": TestHealthcheck{errors.New("Oops")},
			},
			statusCode: 503,
			output: `{
				"status": "FAIL",
				"details": {
					"failing": {
						"status": "FAIL",
						"detail": "Oops"
					}
				}
			}`,
		},
		{
			name: "Mixed Checks",
			checks: map[string]health.Healthcheck{
				"passing": TestHealthcheck{},
				"failing": TestHealthcheck{errors.New("Oops")},
			},
			statusCode: 503,
			output: `{
				"status": "FAIL",
				"details": {
					"failing": {
						"status": "FAIL",
						"detail": "Oops"
					},
					"passing": {
						"status": "SUCCESS"
					}
				}
			}`,
		},
	}

	for _, tc := range testCases {
		tc := tc

		t.Run(tc.name, func(t *testing.T) {
			handler := handlers.NewHealthcheckHandler(tc.checks)

			w := httptest.NewRecorder()
			req := httptest.NewRequest("GET", "http://example.com/health", nil)
			handler.Handle(w, req)

			response := w.Result()

			assert.Equal(t, tc.statusCode, response.StatusCode)
			testutils.AssertJSONResponse(t, tc.output, response)
		})
	}
}
