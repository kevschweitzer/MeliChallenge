package com.example.melichallenge.details

import androidx.lifecycle.ViewModel
import com.example.melichallenge.search.model.entities.SearchResult

class DetailsViewModel(
    val product: SearchResult
): ViewModel()