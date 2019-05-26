package uk.co.grahamcox.goworlds.service.http.problem

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import org.springframework.http.HttpStatus
import java.net.URI

/**
 * RFC-7807 Problem Details representation
 */
data class ProblemModel(
        @JsonProperty("type") val type: URI,
        @JsonProperty("title") val title: String,
        @JsonIgnore val statusCode: HttpStatus,
        @JsonProperty("instance") val instance: URI? = null,
        @JsonProperty("detail") val detail: String? = null,
        @JsonUnwrapped val details: Any? = null
) {
    @JsonProperty("status")
    val status = statusCode.value()
}
