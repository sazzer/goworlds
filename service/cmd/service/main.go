package main

import (
	"fmt"
	"net/http"
	"os"

	"github.com/sazzer/goworlds/service/internal/server"
	"github.com/sirupsen/logrus"
)

func main() {
	logrus.SetOutput(os.Stdout)
	logrus.SetLevel(logrus.DebugLevel)

	config := LoadConfig()

	server := server.New()

	address := fmt.Sprintf(":%d", config.HTTPPort)
	logrus.WithField("address", address).Info("Starting server")
	if err := http.ListenAndServe(address, server); err != nil {
		logrus.WithError(err).Fatal("Failed to start server")
	}
}
