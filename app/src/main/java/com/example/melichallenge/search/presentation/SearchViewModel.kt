package com.example.melichallenge.search.presentation

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melichallenge.search.model.entities.FilterOptions
import com.example.melichallenge.search.model.entities.FilterValue
import com.example.melichallenge.search.model.entities.SearchResponseModel
import com.example.melichallenge.search.model.entities.SearchResult
import com.example.melichallenge.search.model.entities.SortOption
import com.example.melichallenge.search.model.repository.SearchRepository
import kotlinx.coroutines.launch

abstract class SearchViewModel: ViewModel() {
    abstract val filtersVisibility: LiveData<Boolean>
    abstract val filterOptions: LiveData<List<String>>
    abstract val sortOptions: LiveData<List<String>>
    abstract val results: LiveData<List<SearchResult>>
    abstract var selectedSortPosition: Int
    abstract var selectedPriceFilterPosition: Int
    abstract fun search(query: String)
    abstract fun setSelectedSort(position: Int)
    abstract fun onToggleFilterClicked()
    abstract fun restoreState(savedInstanceState: Bundle?)
    abstract fun saveState(outState: Bundle): Bundle
    abstract fun filterBy(position: Int)
}

class SearchViewModelImpl(
    private val searchRepository: SearchRepository
): SearchViewModel() {

    companion object {
        private const val FILTER_VISIBILITY_KEY = "filters_visibilty"
        private const val SORT_SELECTION_KEY = "sort_selection"
        private const val PRICE_FILTER_SELECTION_KEY = "price_filter_selection"
        private const val SELECTED_FILTERS_VALUES_KEY = "selected_filter_values"
    }

    private val _filterOptions = MutableLiveData<List<FilterValue>>()
    override val filterOptions: LiveData<List<String>> get() = Transformations.map(_filterOptions) {
        it.map { it.name }
    }

    private val _sortOptions = MutableLiveData<List<SortOption>>()
    override val sortOptions: LiveData<List<String>> get() = Transformations.map(_sortOptions) {
        it.map { it.name }
    }

    private val _results = MutableLiveData<List<SearchResult>>()
    override val results: LiveData<List<SearchResult>> get() = _results

    override var selectedSortPosition: Int = 0
    override var selectedPriceFilterPosition: Int = 0

    private var _filtersVisibility = MutableLiveData(false)
    override val filtersVisibility: LiveData<Boolean> get() = _filtersVisibility

    private var selectedFilters = FilterOptions()
    private var query: String = ""

    override fun search(query: String) {
        this.query = query
        viewModelScope.launch {
            val searchResponse = searchRepository.searchByKeyword(query, selectedFilters)
            setupViewValues(searchResponse)
        }
    }

    private fun setupViewValues(searchResponse: SearchResponseModel) {
        _results.value = searchResponse.results
        _filterOptions.value = if(searchResponse.priceFilter != null )
            listOf(FilterValue("", "Todos", 0)) + searchResponse.priceFilter.values
        else
            listOf()
        _sortOptions.value = listOf(searchResponse.sort) + searchResponse.availableSorts
    }

    override fun setSelectedSort(position: Int) {
        selectedSortPosition = position
        selectedFilters.sortValue = _sortOptions.value?.get(position)?.id ?: ""
        refreshQuery()
    }

    override fun filterBy(position: Int) {
        selectedPriceFilterPosition = position
        selectedFilters.priceFilterValue =_filterOptions.value?.get(position)?.id ?: ""
        refreshQuery()
    }

    override fun onToggleFilterClicked() {
        _filtersVisibility.value = _filtersVisibility.value?.not()
    }

    override fun restoreState(savedInstanceState: Bundle?) {
        _filtersVisibility.value = savedInstanceState?.getBoolean(FILTER_VISIBILITY_KEY) ?: false
        selectedSortPosition = savedInstanceState?.getInt(SORT_SELECTION_KEY, 0) ?: 0
        selectedPriceFilterPosition = savedInstanceState?.getInt(PRICE_FILTER_SELECTION_KEY, 0) ?: 0
        savedInstanceState?.getSerializable(SELECTED_FILTERS_VALUES_KEY)?.let {
            selectedFilters =  it as FilterOptions
        }
    }

    override fun saveState(outState: Bundle): Bundle {
        return outState.apply {
            putBoolean(FILTER_VISIBILITY_KEY, _filtersVisibility.value?: false)
            putInt(SORT_SELECTION_KEY, selectedSortPosition)
            putInt(PRICE_FILTER_SELECTION_KEY, selectedPriceFilterPosition)
            putSerializable(SELECTED_FILTERS_VALUES_KEY, selectedFilters)
        }
    }

    private fun refreshQuery() {
        viewModelScope.launch {
            _results.value = searchRepository.searchByKeyword(query, selectedFilters).results
        }
    }
}