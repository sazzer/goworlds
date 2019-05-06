package server

import (
	"net/http"
	"time"

	"github.com/go-chi/chi"
	"github.com/go-chi/chi/middleware"
	"github.com/go-chi/cors"
	"github.com/sirupsen/logrus"
)

// New will build a new HTTP Server as needed to run the application
func New(routes ...Route) *chi.Mux {
	r := chi.NewRouter()

	r.Use(middleware.RequestID)
	r.Use(middleware.RealIP)
	r.Use(middleware.Logger)
	r.Use(middleware.Recoverer)
	r.Use(middleware.Timeout(60 * time.Second))

	cors := cors.New(cors.Options{
		AllowedOrigins:   []string{"*"},
		AllowedMethods:   []string{"GET", "PATCH", "POST", "PUT", "DELETE", "OPTIONS"},
		ExposedHeaders:   []string{"Link"},
		AllowCredentials: true,
		MaxAge:           300,
	})
	r.Use(cors.Handler)

	for _, route := range routes {
		logrus.WithField("Method", route.Method).WithField("URL", route.URL).Info("Added Route")

		switch route.Method {
		case http.MethodGet:
			r.Get(route.URL, route.Handler)
		case http.MethodPatch:
			r.Patch(route.URL, route.Handler)
		case http.MethodPut:
			r.Put(route.URL, route.Handler)
		case http.MethodPost:
			r.Post(route.URL, route.Handler)
		case http.MethodDelete:
			r.Delete(route.URL, route.Handler)
		default:
			logrus.WithField("Method", route.Method).WithField("URL", route.URL).Fatal("Unsupported HTTP Method")
		}
	}

	return r
}
