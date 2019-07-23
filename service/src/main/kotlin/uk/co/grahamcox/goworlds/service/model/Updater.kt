package uk.co.grahamcox.goworlds.service.model

/**
 * Interface describing how to update arbitrary data
 */
interface Updater<ID : Id, DATA> {
    /**
     * Update the data with the given lambda
     * @param userId The ID of the data
     * @param modifier The means to mutate the data
     * @return the newly updated data
     */
    fun update(userId: ID, modifier: (Model<ID, DATA>) -> DATA) : Model<ID, DATA>

}