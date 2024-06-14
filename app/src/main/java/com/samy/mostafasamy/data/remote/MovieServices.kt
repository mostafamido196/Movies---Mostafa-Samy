package com.samy.mostafasamy.data.remote

import com.samy.mostafasamy.pojo.response.PopularResponse
import com.samy.mostafasamy.pojo.response.TopRatedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieServices {
    @GET("movie/popular")
    suspend fun getPopular(
        @Query("api_key") apiKey: String ,
    ): Response<PopularResponse>

    @GET("movie/top_rated")
    suspend fun getTopRated(
        @Query("api_key") apiKey: String ,
    ): Response<TopRatedResponse>

//    @GET("search/movie")
//    fun searchMovies(
//        @Query("api_key") apiKey: String?,
//        @Query("query") query: String?,
//        @Query("page") page: Int,
//    ): Call<MovieResponse?>?
//
//    @GET("movie/{movie_id}")
//    fun getMovieDetails(
//        @Path("movie_id") movieId: Int,
//        @Query("api_key") apiKey: String?,
//    ): Call<MovieDetails?>?
}