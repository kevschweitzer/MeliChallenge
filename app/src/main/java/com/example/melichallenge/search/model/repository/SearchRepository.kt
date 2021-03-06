package com.example.melichallenge.search.model.repository

import com.example.melichallenge.search.model.entities.FilterOptions
import com.example.melichallenge.search.model.entities.SearchResponseModel
import com.example.melichallenge.search.model.service.SearchService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface SearchRepository {
    suspend fun searchByKeyword(keyword: String, filterOptions: FilterOptions): SearchResponseModel
}

internal class SearchRepositoryImpl(
    private val searchService: SearchService
): SearchRepository {

    override suspend fun searchByKeyword(keyword: String, filterOptions: FilterOptions) = withContext(Dispatchers.IO) {
        searchService.searchByKeyword(keyword, filterOptions.priceFilterValue, filterOptions.sortValue).toSearchResponseModel()
    }
}