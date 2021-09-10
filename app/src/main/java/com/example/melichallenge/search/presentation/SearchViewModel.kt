package com.example.melichallenge.search.presentation

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melichallenge.search.model.FilterValue
import com.example.melichallenge.search.model.SearchResponseModel
import com.example.melichallenge.search.model.SearchResult
import com.example.melichallenge.search.model.repository.SearchRepository
import kotlinx.coroutines.launch

abstract class SearchViewModel: ViewModel() {
    abstract val filtersVisibility: LiveData<Boolean>
    abstract val selectedSort: LiveData<SortFilters>
    abstract val searchResult: LiveData<SearchResponseModel>
    abstract val results: LiveData<List<SearchResult>>
    abstract fun search(query: String)
    abstract fun setSelectedSort(position: Int)
    abstract fun onToggleFilterClicked()
    abstract fun restoreState(savedInstanceState: Bundle?)
    abstract fun saveState(outState: Bundle): Bundle
    abstract fun filterBy(position: Int)
}

data class FilterOptions(
    var priceFilterValue: String = ""
)

class SearchViewModelImpl(
    private val searchRepository: SearchRepository
): SearchViewModel() {

    companion object {
        private const val FILTER_VISIBILITY_KEY = "filters_visibilty"
        private const val SORT_SELECTION_KEY = "sort_selection"
    }

    private val _searchResult = MutableLiveData<SearchResponseModel>()
    override val searchResult: LiveData<SearchResponseModel> = _searchResult

    private val _results = MutableLiveData<List<SearchResult>>()
    override val results: LiveData<List<SearchResult>> = _results

    private var _selectedSort = MutableLiveData(SortFilters.LOWER_PRICE)
    override val selectedSort: LiveData<SortFilters> get() = _selectedSort

    private var _filtersVisibility = MutableLiveData(false)
    override val filtersVisibility: LiveData<Boolean> get() = _filtersVisibility

    private val filterOptions = FilterOptions()
    private var query: String = ""

    override fun search(query: String) {
        this.query = query
        viewModelScope.launch {
            val searchResult = searchRepository.searchByKeyword(query, filterOptions).apply {
                priceFilter?.values = listOf(FilterValue("", "Todos", 0)) + priceFilter!!.values
            }
            _searchResult.value = searchResult
            _results.value = searchResult.results
        }
    }

    override fun setSelectedSort(position: Int) {
        when(position) {
            SortFilters.HIGHER_PRICE.ordinal -> {
                _selectedSort.value = SortFilters.HIGHER_PRICE
            }
            else -> {
                _selectedSort.value = SortFilters.LOWER_PRICE
            }
        }
        refreshResults()
    }

    private fun refreshResults() {
        _results.value?.let {
            _results.value = sort(it)
        }
    }

    private fun sort(results: List<SearchResult>) = when(_selectedSort.value) {
        SortFilters.HIGHER_PRICE -> results.sortedByDescending { it.price }
        else -> results.sortedBy { it.price }
    }

    override fun onToggleFilterClicked() {
        _filtersVisibility.value = _filtersVisibility.value?.not()
    }

    override fun restoreState(savedInstanceState: Bundle?) {
        _filtersVisibility.value = savedInstanceState?.getBoolean(FILTER_VISIBILITY_KEY) ?: false
        _selectedSort.value = SortFilters.values()[savedInstanceState?.getInt(SORT_SELECTION_KEY, 0) ?: 0]
    }

    override fun saveState(outState: Bundle): Bundle {
        return outState.apply {
            putBoolean(FILTER_VISIBILITY_KEY, _filtersVisibility.value?: false)
            putInt(SORT_SELECTION_KEY, _selectedSort.value?.ordinal ?: 0)
        }
    }

    override fun filterBy(position: Int) {
        filterOptions.priceFilterValue =_searchResult.value?.priceFilter?.values?.get(position)?.id ?: ""
        viewModelScope.launch {
            _results.value = sort(searchRepository.searchByKeyword(query, filterOptions).results)
        }
    }
}