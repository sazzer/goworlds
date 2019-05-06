package handlers

// healthcheckComponentModel represents the HTTP Response Model for a single Healthcheck component
type healthcheckComponentModel struct {
	Status string `json:"status"`
	Detail string `json:"detail,omitempty"`
}

// healthcheckModel represents the HTTP Response Model for the overall healthchecks
type healthcheckModel struct {
	Status  string                               `json:"status"`
	Details map[string]healthcheckComponentModel `json:"details"`
}
