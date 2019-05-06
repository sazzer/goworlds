package handlers

import (
	"net/http"

	"github.com/sazzer/goworlds/service/internal/health"
	"github.com/sazzer/goworlds/service/internal/server"
)

const (
	statusSuccess = "SUCCESS"
	statusFail    = "FAIL"
)

// HealthcheckHandler represents the actual HTTP Handler for checking the health of the system
type HealthcheckHandler struct {
	checks map[string]health.Healthcheck
}

// NewHealthcheckHandler will create a new handler for checking the healthchecks
func NewHealthcheckHandler(checks map[string]health.Healthcheck) HealthcheckHandler {
	return HealthcheckHandler{checks: checks}
}

// Handle is the actual HTTP Handler for the healthchecks
func (handler HealthcheckHandler) Handle(w http.ResponseWriter, r *http.Request) {
	result := healthcheckModel{
		Status:  statusSuccess,
		Details: map[string]healthcheckComponentModel{},
	}

	statusCode := http.StatusOK

	for k, v := range handler.checks {
		if status := v.CheckHealth(); status != nil {
			result.Details[k] = healthcheckComponentModel{
				Status: statusFail,
				Detail: status.Error(),
			}
			result.Status = statusFail
			statusCode = http.StatusServiceUnavailable
		} else {
			result.Details[k] = healthcheckComponentModel{
				Status: statusSuccess,
			}
		}
	}

	server.JSONResponse{
		StatusCode: statusCode,
		Body:       result,
	}.Write(w)
}
