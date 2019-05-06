package main

import (
	"os"

	"github.com/sirupsen/logrus"
)

func main() {
	logrus.SetOutput(os.Stdout)
	logrus.SetLevel(logrus.DebugLevel)

	config := LoadConfig()

	logrus.WithField("config", config).Info("Hello")
}
