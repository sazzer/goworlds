package healthwiring

import (
	"net/http"

	"github.com/sazzer/goworlds/service/internal/health"
	"github.com/sazzer/goworlds/service/internal/health/handlers"
	"github.com/sazzer/goworlds/service/internal/server"
)

// Wiring represents the built healthchecks module
type Wiring struct {
	Route server.Route
}

// New constructs the Healthchecks module
func New(checks map[string]health.Healthcheck) Wiring {
	handler := handlers.NewHealthcheckHandler(checks)

	return Wiring{
		Route: server.Route{
			URL:     "/health",
			Method:  http.MethodGet,
			Handler: handler.Handle,
		},
	}
}
