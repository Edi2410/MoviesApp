package com.example.moviesapp.factory

import android.content.Context
import com.example.moviesapp.dao.MoviesSqlHelper

fun getRepository(context: Context) = MoviesSqlHelper(context)
