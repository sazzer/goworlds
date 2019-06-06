package uk.co.grahamcox.goworlds.service.http.problems

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * The problem details for an invalid sort
 */
data class InvalidSortsDetail(
        @JsonProperty("unknown-sorts") val unknownSorts: List<String>
)
