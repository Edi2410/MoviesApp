package com.example.moviesapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.algebra.nasaapp.framework.setBooleanPreference
import hr.algebra.nasaapp.framework.startActivity

class MoviesReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.setBooleanPreference(DATA_IMPORTED)
        context.startActivity<HostActivity>()
    }
}