package com.example.melichallenge.search.model.service

import com.example.melichallenge.search.model.FilterValue
import com.example.melichallenge.search.model.ResultFilter
import com.example.melichallenge.search.model.SearchResponseModel
import com.example.melichallenge.search.model.SearchResponsePaging
import com.example.melichallenge.search.model.SearchResult
import com.example.melichallenge.search.model.SortOption
import com.google.gson.annotations.SerializedName

data class ServerSearchResponse(
    @SerializedName("site_id") val siteId: String,
    val query: String,
    val paging: ServerSearchResponsePaging,
    val results: List<ServerSearchResult>,
    @SerializedName("available_filters") val availableFilters: List<ServerResultFilter>,
    val sort: SortOptionResponse,
    @SerializedName("available_sorts") val availableSorts: List<SortOptionResponse>
) {
    fun toSearchResponseModel() = SearchResponseModel(
        siteId,
        query,
        paging.toSearchResponsePaging(),
        results.map { it.toSearchResult() },
        availableFilters.find { it.id == "price" }?.toResultFilter(),
        sort.toSortOption(),
        availableSorts.map { it.toSortOption() }
    )
}

data class ServerSearchResult(
    @SerializedName("id") val productId: String,
    val title: String,
    val price: Float,
    val thumbnail: String
) {
    fun toSearchResult() = SearchResult(productId, title, price, thumbnail)
}

data class ServerSearchResponsePaging(
    val total: Int,
    val offset: Int,
    val limit: Int,
    @SerializedName("primary_results") var primaryResults: Int
) {
    fun toSearchResponsePaging() = SearchResponsePaging(
        total, offset, limit, primaryResults
    )
}

data class ServerResultFilter(
    val id: String,
    val name: String,
    val type: String,
    val values: List<ServerFilterValue>
) {
    fun toResultFilter() = ResultFilter(
        id,
        name,
        type,
        values.map { it.toFilterValue() }
    )
}

data class ServerFilterValue(
    val id: String,
    val name: String,
    val results: Int
) {
    fun toFilterValue() = FilterValue(
        id,
        name,
        results
    )
}

data class SortOptionResponse(
    val id: String,
    val name: String
) {
    fun toSortOption() = SortOption(id, name)
}