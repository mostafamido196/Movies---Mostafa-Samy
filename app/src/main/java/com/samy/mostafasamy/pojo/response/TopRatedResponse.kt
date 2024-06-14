package com.samy.mostafasamy.pojo.response

data class TopRatedResponse(
    val page: Int,
    val results: List<ResultX>,
    val total_pages: Int,
    val total_results: Int
)