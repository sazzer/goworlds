package uk.co.grahamcox.goworlds.service.model

/**
 * Interface describing how to create arbitrary data
 */
interface Creator<ID : Id, DATA> {
    /**
     * Create a new record
     * @param data The data for the record
     * @return the created data
     */
    fun create(data: DATA) : Model<ID, DATA>

}