// Package zipdemographics provides a Go client for the ZIP Demographics API.
//
// For more information, visit: https://apiverve.com/marketplace/zipdemographics?utm_source=go&utm_medium=readme
package zipdemographics

import (
	"fmt"
	"reflect"
	"regexp"
	"strings"
)

// ValidationRule defines validation constraints for a parameter.
type ValidationRule struct {
	Type      string
	Required  bool
	Min       *float64
	Max       *float64
	MinLength *int
	MaxLength *int
	Format    string
	Enum      []string
}

// ValidationError represents a parameter validation error.
type ValidationError struct {
	Errors []string
}

func (e *ValidationError) Error() string {
	return "Validation failed: " + strings.Join(e.Errors, "; ")
}

// Helper functions for pointers
func float64Ptr(v float64) *float64 { return &v }
func intPtr(v int) *int             { return &v }

// Format validation patterns
var formatPatterns = map[string]*regexp.Regexp{
	"email":    regexp.MustCompile(`^[^\s@]+@[^\s@]+\.[^\s@]+$`),
	"url":      regexp.MustCompile(`^https?://.+`),
	"ip":       regexp.MustCompile(`^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$|^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$`),
	"date":     regexp.MustCompile(`^\d{4}-\d{2}-\d{2}$`),
	"hexColor": regexp.MustCompile(`^#?([0-9a-fA-F]{3}|[0-9a-fA-F]{6})$`),
}

// Request contains the parameters for the ZIP Demographics API.
//
// Parameters:
//   - zip (required): string - 5-digit US ZIP code [minLength: 5, maxLength: 5]
type Request struct {
	Zip string `json:"zip"` // Required
}

// ToQueryParams converts the request struct to a map of query parameters.
// Only non-zero values are included.
func (r *Request) ToQueryParams() map[string]string {
	params := make(map[string]string)
	if r == nil {
		return params
	}

	v := reflect.ValueOf(*r)
	t := v.Type()

	for i := 0; i < v.NumField(); i++ {
		field := v.Field(i)
		fieldType := t.Field(i)

		// Get the json tag for the field name
		jsonTag := fieldType.Tag.Get("json")
		if jsonTag == "" {
			continue
		}
		// Handle tags like `json:"name,omitempty"`
		jsonName := strings.Split(jsonTag, ",")[0]
		if jsonName == "-" {
			continue
		}

		// Skip zero values
		if field.IsZero() {
			continue
		}

		// Convert to string
		params[jsonName] = fmt.Sprintf("%v", field.Interface())
	}

	return params
}

// Validate checks the request parameters against validation rules.
// Returns a ValidationError if validation fails, nil otherwise.
func (r *Request) Validate() error {
	rules := map[string]ValidationRule{
		"zip": {Type: "string", Required: true, MinLength: intPtr(5), MaxLength: intPtr(5)},
	}

	if len(rules) == 0 {
		return nil
	}

	var errors []string
	v := reflect.ValueOf(*r)
	t := v.Type()

	for i := 0; i < v.NumField(); i++ {
		field := v.Field(i)
		fieldType := t.Field(i)

		jsonTag := fieldType.Tag.Get("json")
		if jsonTag == "" {
			continue
		}
		jsonName := strings.Split(jsonTag, ",")[0]

		rule, exists := rules[jsonName]
		if !exists {
			continue
		}

		// Check required
		if rule.Required && field.IsZero() {
			errors = append(errors, fmt.Sprintf("Required parameter [%s] is missing", jsonName))
			continue
		}

		if field.IsZero() {
			continue
		}

		// Type-specific validation
		switch rule.Type {
		case "integer", "number":
			var numVal float64
			switch field.Kind() {
			case reflect.Int, reflect.Int64:
				numVal = float64(field.Int())
			case reflect.Float64:
				numVal = field.Float()
			}
			if rule.Min != nil && numVal < *rule.Min {
				errors = append(errors, fmt.Sprintf("Parameter [%s] must be at least %v", jsonName, *rule.Min))
			}
			if rule.Max != nil && numVal > *rule.Max {
				errors = append(errors, fmt.Sprintf("Parameter [%s] must be at most %v", jsonName, *rule.Max))
			}

		case "string":
			strVal := field.String()
			if rule.MinLength != nil && len(strVal) < *rule.MinLength {
				errors = append(errors, fmt.Sprintf("Parameter [%s] must be at least %d characters", jsonName, *rule.MinLength))
			}
			if rule.MaxLength != nil && len(strVal) > *rule.MaxLength {
				errors = append(errors, fmt.Sprintf("Parameter [%s] must be at most %d characters", jsonName, *rule.MaxLength))
			}
			if rule.Format != "" {
				if pattern, ok := formatPatterns[rule.Format]; ok {
					if !pattern.MatchString(strVal) {
						errors = append(errors, fmt.Sprintf("Parameter [%s] must be a valid %s", jsonName, rule.Format))
					}
				}
			}
		}

		// Enum validation
		if len(rule.Enum) > 0 {
			strVal := fmt.Sprintf("%v", field.Interface())
			found := false
			for _, enumVal := range rule.Enum {
				if strVal == enumVal {
					found = true
					break
				}
			}
			if !found {
				errors = append(errors, fmt.Sprintf("Parameter [%s] must be one of: %s", jsonName, strings.Join(rule.Enum, ", ")))
			}
		}
	}

	if len(errors) > 0 {
		return &ValidationError{Errors: errors}
	}
	return nil
}

// ResponseData contains the data returned by the ZIP Demographics API.
type ResponseData struct {
	Zip string `json:"zip"`
	Name string `json:"name"`
	AcsYear int `json:"acsYear"`
	Population PopulationData `json:"population"`
	Income IncomeData `json:"income"`
	Housing HousingData `json:"housing"`
	Education EducationData `json:"education"`
	Employment EmploymentData `json:"employment"`
	Race RaceData `json:"race"`
}

// PopulationData represents the population object.
type PopulationData struct {
	Total int `json:"total"`
	Male int `json:"male"`
	Female int `json:"female"`
	MedianAge float64 `json:"medianAge"`
}

// IncomeData represents the income object.
type IncomeData struct {
	MedianHousehold int `json:"medianHousehold"`
	PerCapita int `json:"perCapita"`
}

// HousingData represents the housing object.
type HousingData struct {
	MedianHomeValue int `json:"medianHomeValue"`
	MedianRent int `json:"medianRent"`
	TotalUnits int `json:"totalUnits"`
	OccupiedUnits int `json:"occupiedUnits"`
	VacantUnits int `json:"vacantUnits"`
	OwnerOccupied int `json:"ownerOccupied"`
	RenterOccupied int `json:"renterOccupied"`
	HomeOwnershipRate float64 `json:"homeOwnershipRate"`
}

// EducationData represents the education object.
type EducationData struct {
	CollegeEducatedPct float64 `json:"collegeEducatedPct"`
	Bachelors int `json:"bachelors"`
	Masters int `json:"masters"`
	Professional int `json:"professional"`
	Doctorate int `json:"doctorate"`
}

// EmploymentData represents the employment object.
type EmploymentData struct {
	LaborForce int `json:"laborForce"`
	Unemployed int `json:"unemployed"`
	UnemploymentRate float64 `json:"unemploymentRate"`
}

// RaceData represents the race object.
type RaceData struct {
	White WhiteData `json:"white"`
	Black BlackData `json:"black"`
	Asian AsianData `json:"asian"`
	Hispanic HispanicData `json:"hispanic"`
}

// WhiteData represents the white object.
type WhiteData struct {
	Count int `json:"count"`
	Percent float64 `json:"percent"`
}

// BlackData represents the black object.
type BlackData struct {
	Count int `json:"count"`
	Percent float64 `json:"percent"`
}

// AsianData represents the asian object.
type AsianData struct {
	Count int `json:"count"`
	Percent float64 `json:"percent"`
}

// HispanicData represents the hispanic object.
type HispanicData struct {
	Count int `json:"count"`
	Percent float64 `json:"percent"`
}
