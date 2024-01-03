package com.example.moviesapp.api

import android.content.Context
import android.content.Intent
import com.example.moviesapp.MoviesReceiver
import hr.algebra.nasaapp.framework.sendBroadcast

class MoviesFetcher (private val context: Context) {
    fun fetchItems(count: Int){
        Thread.sleep(3000)

        context.sendBroadcast<MoviesReceiver>()
    }
}