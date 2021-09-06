package com.example.melichallenge.search.model

import com.google.gson.annotations.SerializedName

data class SearchResponseModel(
    @SerializedName("site_id") val siteId: String,
    val query: String,
    val paging: SearchResponsePaging,
    val results: List<SearchResult>
)

data class SearchResult(
    val productId: String,
    val title: String,
    val price: Float
)

data class SearchResponsePaging(
    val total: Int,
    val offset: Int,
    val limit: Int,
    @SerializedName("primary_results") var primaryResults: Int
)