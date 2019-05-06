package health

// Healthcheck represents anything that can indicate it's health
type Healthcheck interface {
	// CheckHealth will check the health of this component
	CheckHealth() error
}
