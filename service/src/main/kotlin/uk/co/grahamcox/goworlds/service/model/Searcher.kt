package uk.co.grahamcox.goworlds.service.model

/**
 * Interface describing how to search for arbitrary data
 */
interface Searcher<ID : Id, DATA, FILTERS, SORT : Enum<SORT>> {

    /**
     * Search for data in the system
     * @param filters Filters to apply when searching
     * @param sorts Sorts to apply for the returned data
     * @param offset The offset of the results to return
     * @param count The count of results to return
     * @return the matching data
     */
    fun search(filters: FILTERS,
               sorts: List<Sort<SORT>> = emptyList(),
               offset: Long = 0,
               count: Long = 10) : Page<Model<ID, DATA>>
}