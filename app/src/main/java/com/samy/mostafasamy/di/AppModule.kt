package com.samy.mostafasamy.di

import androidx.room.Room
import com.samy.mostafasamy.data.local.AppDatabase
import com.samy.mostafasamy.data.remote.MovieServices
import com.samy.mostafasamy.utils.Constants.BASE_URL
//import com.samy.mostafasamy.data.AddRequirementsServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val TIMEOUT = 30L

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun providesOkHttp(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder().apply {
            /*   if (BuildConfig.DEBUG) {
                   addInterceptor(HttpLoggingInterceptor().apply {
                       level = HttpLoggingInterceptor.Level.BODY
                   })
               }*/
            addInterceptor(httpLoggingInterceptor)
            connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            readTimeout(TIMEOUT, TimeUnit.SECONDS)
        }.build()

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder().apply {
        baseUrl(BASE_URL)
        client(okHttpClient)
        addConverterFactory(GsonConverterFactory.create())
    }.build()

    @Provides
    @Singleton
    fun provideMovieServices(retrofit: Retrofit): MovieServices {
        return retrofit.create(MovieServices::class.java)
    }

//    @Provides
//    @Singleton
//    fun providesRoom(applicationContext :ApplicationContext): AppDatabase {
//        return Room.databaseBuilder(
//            applicationContext,
//            AppDatabase::class.java,
//            "my_database"
//        ).build()
//    }


}