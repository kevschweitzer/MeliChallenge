package com.example.melichallenge.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melichallenge.search.model.SearchResult
import com.example.melichallenge.search.model.repository.SearchRepository
import kotlinx.coroutines.launch

abstract class SearchViewModel: ViewModel() {
    abstract var searchText: String
    abstract var searchResult: LiveData<List<SearchResult>>
    abstract fun search()
}

class SearchViewModelImpl(
    private val searchRepository: SearchRepository
): SearchViewModel() {

    override var searchText: String = ""
    private val _searchResult = MutableLiveData<List<SearchResult>>()
    override var searchResult: LiveData<List<SearchResult>> = _searchResult

    override fun search() {
        viewModelScope.launch {
            _searchResult.value = searchRepository.searchByKeyword(searchText).results
        }
    }
}