package main

import (
	"fmt"
	"net/http"
	"os"

	"github.com/sazzer/goworlds/service/internal/health"
	"github.com/sazzer/goworlds/service/internal/health/healthwiring"
	"github.com/sazzer/goworlds/service/internal/server"
	"github.com/sirupsen/logrus"
)

type Healthcheck struct {
	status error
}

func (s Healthcheck) CheckHealth() error {
	return s.status
}

func main() {
	logrus.SetOutput(os.Stdout)
	logrus.SetLevel(logrus.DebugLevel)

	config := LoadConfig()

	healthchecks := healthwiring.New(map[string]health.Healthcheck{
		"database": Healthcheck{nil},
	})

	server := server.New(healthchecks.Route)

	address := fmt.Sprintf(":%d", config.HTTPPort)
	logrus.WithField("address", address).Info("Starting server")
	if err := http.ListenAndServe(address, server); err != nil {
		logrus.WithError(err).Fatal("Failed to start server")
	}
}
