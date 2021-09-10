package com.example.melichallenge.di

import com.example.melichallenge.details.DetailsViewModel
import com.example.melichallenge.search.presentation.SearchViewModel
import com.example.melichallenge.search.presentation.SearchViewModelImpl
import com.example.melichallenge.search.model.entities.SearchResult
import com.example.melichallenge.welcome.WelcomeViewModel
import com.example.melichallenge.welcome.WelcomeViewModelImpl
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<SearchViewModel> {
        SearchViewModelImpl(get())
    }

    viewModel { (product: SearchResult) ->
        DetailsViewModel(product)
    }

    viewModel<WelcomeViewModel> {
        WelcomeViewModelImpl()
    }
}