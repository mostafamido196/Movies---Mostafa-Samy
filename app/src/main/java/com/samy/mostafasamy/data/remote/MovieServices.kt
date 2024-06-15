package com.samy.mostafasamy.data.remote

import com.samy.mostafasamy.pojo.response.DetailResponse
import com.samy.mostafasamy.pojo.response.PopularResponse
import com.samy.mostafasamy.pojo.response.SearchResponse
import com.samy.mostafasamy.pojo.response.TopRatedResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieServices {
    @GET("movie/popular")
    suspend fun getPopular(
        @Query("api_key") apiKey: String,
    ): Response<PopularResponse>

    @GET("movie/top_rated")
    suspend fun getTopRated(
        @Query("api_key") apiKey: String,
    ): Response<TopRatedResponse>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
    ): Response<SearchResponse?>


        @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
    ): Response<DetailResponse?>


}