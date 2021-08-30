package com.example.melichallenge.search.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melichallenge.search.model.repository.SearchRepository
import kotlinx.coroutines.launch

abstract class SearchViewModel: ViewModel() {
    abstract var searchText: String
    abstract fun search()
}

class SearchViewModelImpl(
    private val searchRepository: SearchRepository
): SearchViewModel() {

    override var searchText: String = ""

    override fun search() {
        viewModelScope.launch {
            searchRepository.searchByKeyword(searchText).results.forEach {
                Log.e("Product", "${it.title} costs ${it.price}")
            }
        }
    }
}