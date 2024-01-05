package com.example.moviesapp.api

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.moviesapp.MOVIES_PROVIDER_CONTENT_URI
import com.example.moviesapp.MoviesReceiver
import com.example.moviesapp.handler.downloadImageAndStore
import hr.algebra.nasaapp.framework.sendBroadcast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MoviesFetcher(private val context: Context) {
    private val moviesApi: MoviesApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val originalRequest: Request = chain.request()

                        // Add headers to the original request
                        val modifiedRequest: Request = originalRequest.newBuilder()
                            .addHeader("accept", "application/json")
                            .addHeader(
                                "Authorization",
                                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiZGI2MDU3YTI5YjZmYmU5ZTM2NTNlOTY2MmFmNTY4NSIsInN1YiI6IjY1NjQ3MmY0ZDk1NTRiMDBjNjE3NWFiMyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.yWmjtY4VMe1kOXevhua-6T64aUmoCvFCUF1RcwAZavU"
                            )
                            .build()

                        chain.proceed(modifiedRequest)
                    }
                    .build()
            )
            .build()
        moviesApi = retrofit.create(MoviesApi::class.java)
    }


    fun fetchItems(page: Int) {
        val request = moviesApi.fetchItems(page)
        request.enqueue(object : Callback<MoviesDiscoverItem> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<MoviesDiscoverItem>,
                response: Response<MoviesDiscoverItem>
            ) {
                response.body()?.let { populateItems(it) }
            }

            override fun onFailure(call: Call<MoviesDiscoverItem>, t: Throwable) {
                Log.e(javaClass.name, t.toString(), t)
            }

        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun populateItems(moviesDiscoverItem: MoviesDiscoverItem) {

        val scope = CoroutineScope(Dispatchers.IO)
        scope.run {
            moviesDiscoverItem.results.forEach {
                val posterPath = downloadImageAndStore(context, it.poster_path)
                val values = ContentValues().apply {
                    put(Item::id.name, it.id)
                    put(Item::title.name, it.title)
                    put(Item::overview.name, it.overview)
                    put(Item::poster_path.name, posterPath?: "")
                    put(Item::release_date.name, it.release_date)
                    put(Item::vote_average.name, it.vote_average)
                    put(Item::vote_count.name, it.vote_count)
                    put(Item::popularity.name, it.popularity)
                    put(Item::backdrop_path.name, it.backdrop_path)
                    put(Item::original_language.name, it.original_language)
                    put(Item::original_title.name, it.original_title)
                    put(Item::adult.name, it.adult)
                    put(Item::video.name, it.video)
                    put(Item::genre_ids.name, it.genre_ids.joinToString(","))

                }
                context.contentResolver.insert(
                    MOVIES_PROVIDER_CONTENT_URI,
                    values
                )

            }

            context.sendBroadcast<MoviesReceiver>()
        }


    }
}