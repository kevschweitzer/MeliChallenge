package com.example.melichallenge.search.model.service

import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("MLA/search")
    suspend fun searchByKeyword(
        @Query("q") keyword: String
    ): ServerSearchResponse
}
