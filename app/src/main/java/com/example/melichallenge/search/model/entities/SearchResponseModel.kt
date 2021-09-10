package com.example.melichallenge.search.model.entities

import java.io.Serializable

data class SearchResponseModel(
    val siteId: String,
    val query: String,
    val paging: SearchResponsePaging,
    val results: List<SearchResult>,
    val priceFilter: ResultFilter?,
    val sort: SortOption,
    val availableSorts: List<SortOption>
)

data class SearchResult(
    val productId: String,
    val title: String,
    val price: Float,
    private val thumbnail: String,
    val availableQuantity: Int,
    val soldQuantity: Int,
    val acceptsMercadopago: Boolean
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

data class SortOption(
    val id: String,
    val name: String
)