package com.example.moviesapp.api

import com.google.gson.annotations.SerializedName

data class MoviesDiscoverItem(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<MovieItem>,
    @SerializedName("total_pages") val total_pages: Int,
    @SerializedName("total_results") val total_results: Int
)
