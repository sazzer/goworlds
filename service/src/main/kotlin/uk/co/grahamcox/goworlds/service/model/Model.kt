package uk.co.grahamcox.goworlds.service.model

/**
 * Actual representation of a persisted model
 * @property identity The identity of the model
 * @property data The data of the model
 */
data class Model<ID : Id, DATA>(
    val identity: Identity<ID>,
    val data: DATA
)
