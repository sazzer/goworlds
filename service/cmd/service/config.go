package main

import (
	"github.com/joho/godotenv"
	"github.com/sirupsen/logrus"
	"github.com/vrischmann/envconfig"
)

// Config defines the configuration of the application
type Config struct {
	// HTTPPort is the port to listen on for the HTTP Server
	HTTPPort int `envconfig:"PORT,default=3000"`

	// DatabaseURL is the connection string for the Postgres Database
	DatabaseURL string `envconfig:"DATABASE_URL"`
}

// LoadConfig will load the configuration for the application
func LoadConfig() Config {
	_ = godotenv.Load()

	var conf Config

	if err := envconfig.Init(&conf); err != nil {
		logrus.WithError(err).Fatal("Failed to load config")
	}
	return conf
}
