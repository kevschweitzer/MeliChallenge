package com.example.melichallenge.search.model.repository

import com.example.melichallenge.search.model.SearchResponseModel
import com.example.melichallenge.search.model.service.SearchService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface SearchRepository {
    suspend fun searchByKeyword(keyword: String): SearchResponseModel
}

internal class SearchRepositoryImpl(
    private val searchService: SearchService
): SearchRepository {

    override suspend fun searchByKeyword(keyword: String) = withContext(Dispatchers.IO) {
        searchService.searchByKeyword(keyword).toSearchResponseModel()
    }
}