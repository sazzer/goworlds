package server

import (
	"encoding/json"
	"net/http"

	"github.com/sirupsen/logrus"
)

// JSONResponse represents a response to an HTTP Request with a JSON Encoded body
type JSONResponse struct {
	Body       interface{}
	StatusCode int
}

// Write will write this response to the given HTTP Response Writer
func (r JSONResponse) Write(w http.ResponseWriter) {
	w.Header().Set("Content-Type", "application/json")
	if r.StatusCode != 0 {
		w.WriteHeader(r.StatusCode)
	} else {
		w.WriteHeader(http.StatusOK)
	}

	if err := json.NewEncoder(w).Encode(r.Body); err != nil {
		logrus.WithError(err).Error("Failed to write JSON Response")
	}
}
