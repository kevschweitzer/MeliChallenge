package com.example.melichallenge.search.model

import java.io.Serializable

data class SearchResponseModel(
    val siteId: String,
    val query: String,
    val paging: SearchResponsePaging,
    val results: List<SearchResult>
)

data class SearchResult(
    val productId: String,
    val title: String,
    val price: Float
): Serializable

data class SearchResponsePaging(
    val total: Int,
    val offset: Int,
    val limit: Int,
    var primaryResults: Int
)