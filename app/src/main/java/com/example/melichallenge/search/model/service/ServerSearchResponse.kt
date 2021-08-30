package com.example.melichallenge.search.model.service

import com.example.melichallenge.search.model.SearchResponseModel
import com.example.melichallenge.search.model.SearchResponsePaging
import com.example.melichallenge.search.model.SearchResult
import com.google.gson.annotations.SerializedName

data class ServerSearchResponse(
    @SerializedName("site_id") val siteId: String,
    val query: String,
    val paging: ServerSearchResponsePaging,
    val results: List<ServerSearchResult>
) {
    fun toSearchResponseModel() = SearchResponseModel(
        siteId,
        query,
        paging.toSearchResponsePaging(),
        results.map { it.toSearchResult() }
    )
}

data class ServerSearchResult(
    val title: String,
    val price: Float
) {
    fun toSearchResult() = SearchResult(title, price)
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