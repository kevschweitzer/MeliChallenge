package com.example.melichallenge.di

import com.example.melichallenge.search.model.repository.SearchRepository
import com.example.melichallenge.search.model.repository.SearchRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    factory<SearchRepository> {
        SearchRepositoryImpl(get())
    }
}