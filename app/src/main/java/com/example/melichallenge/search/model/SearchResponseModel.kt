package com.example.melichallenge.search.model

import java.io.Serializable

data class SearchResponseModel(
    val siteId: String,
    val query: String,
    val paging: SearchResponsePaging,
    val results: List<SearchResult>,
    val priceFilter: ResultFilter?
)

data class SearchResult(
    val productId: String,
    val title: String,
    val price: Float,
    private val thumbnail: String
): Serializable {
    val thumbnailUrl: String get() = thumbnail.replace("http://", "https://")
}

data class SearchResponsePaging(
    val total: Int,
    val offset: Int,
    val limit: Int,
    var primaryResults: Int
)

data class ResultFilter(
    val id: String,
    val name: String,
    val type: String,
    var values: List<FilterValue>
)

data class FilterValue(
    val id: String,
    val name: String,
    val results: Int
)