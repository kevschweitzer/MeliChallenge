package com.example.melichallenge.search

import android.util.Log
import androidx.lifecycle.ViewModel

abstract class SearchViewModel: ViewModel() {
    abstract var searchText: String
    abstract fun search()
}

class SearchViewModelImpl: SearchViewModel() {

    override var searchText: String = ""

    override fun search() {
        Log.e("asd", searchText)
    }
}