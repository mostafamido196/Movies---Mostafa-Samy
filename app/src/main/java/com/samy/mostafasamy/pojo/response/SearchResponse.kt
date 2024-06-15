package com.samy.mostafasamy.pojo.response

data class SearchResponse(
    val page: Int,
    val results: List<ResultXX>,
    val total_pages: Int,
    val total_results: Int
)