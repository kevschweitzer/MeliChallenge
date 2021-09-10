package com.example.melichallenge.search.model.entities

import java.io.Serializable

data class FilterOptions(
    var priceFilterValue: String = "",
    var sortValue: String = ""
): Serializable