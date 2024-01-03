package com.example.moviesapp.api

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class MoviesWorker(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {
        MoviesFetcher(context).fetchItems(10)
        return Result.success()
    }

}