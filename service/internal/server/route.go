package server

import "net/http"

// Route represents a single route definition for the server
type Route struct {
	URL     string
	Method  string
	Handler http.HandlerFunc
}
