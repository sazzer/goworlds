package uk.co.grahamcox.goworlds.service.model

/**
 * Interface describing how to retrieve arbitrary data
 */
interface Retriever<ID : Id, DATA> {
    /**
     * Retrieve the data by it's unique ID
     * @param id The ID
     * @return the data
     */
    fun getById(id : ID) : Model<ID, DATA>
}