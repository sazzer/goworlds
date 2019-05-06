package main

import (
	"fmt"
	"net/http"
	"os"

	"github.com/sazzer/goworlds/service/internal/database"
	"github.com/sazzer/goworlds/service/internal/health"
	"github.com/sazzer/goworlds/service/internal/health/healthwiring"
	"github.com/sazzer/goworlds/service/internal/server"
	"github.com/sirupsen/logrus"

	_ "github.com/lib/pq"
)

func main() {
	logrus.SetOutput(os.Stdout)
	logrus.SetLevel(logrus.DebugLevel)

	config := LoadConfig()

	database, err := database.NewFromURL(config.DatabaseURL)
	if err != nil {
		logrus.WithError(err).Fatal("Failed to connect to database")
	}

	healthchecks := healthwiring.New(map[string]health.Healthcheck{
		"database": database,
	})

	server := server.New(healthchecks.Route)

	address := fmt.Sprintf(":%d", config.HTTPPort)
	logrus.WithField("address", address).Info("Starting server")
	if err := http.ListenAndServe(address, server); err != nil {
		logrus.WithError(err).Fatal("Failed to start server")
	}
}
