package com.example.melichallenge.di

import com.example.melichallenge.search.presentation.SearchViewModel
import com.example.melichallenge.search.presentation.SearchViewModelImpl
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel<SearchViewModel> { SearchViewModelImpl(get()) }
}