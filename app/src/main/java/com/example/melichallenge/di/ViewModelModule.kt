package com.example.melichallenge.di

import com.example.melichallenge.details.DetailsViewModel
import com.example.melichallenge.search.presentation.SearchViewModel
import com.example.melichallenge.search.presentation.SearchViewModelImpl
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val SHARED_SCOPE_SEARCH_ID = "scope_search_id"

val viewModelModule = module {

    scope<SearchViewModel> {
        scoped<SearchViewModel> { SearchViewModelImpl(get()) }
    }

    viewModel { (productId: String) ->
        DetailsViewModel(productId)
    }
}