package com.example.moviesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.moviesapp.api.MoviesWorker
import com.example.moviesapp.databinding.ActivitySplashScreenBinding
import hr.algebra.nasaapp.framework.applyAnimation
import hr.algebra.nasaapp.framework.callDelayed
import hr.algebra.nasaapp.framework.getBooleanPreference
import hr.algebra.nasaapp.framework.isOnline
import hr.algebra.nasaapp.framework.startActivity

private const val DELAY = 3000L

const val DATA_IMPORTED = "com.example.moviesapp.data_imported"

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimations()
        redirect()
    }

    private fun startAnimations() {
        binding.ivSplash.applyAnimation(R.anim.rotate)
        binding.tvSplash.applyAnimation(R.anim.scale_pulse)
    }


    private fun redirect() {

        if (
            getBooleanPreference(DATA_IMPORTED)
        ) {
            callDelayed(DELAY) { startActivity<HostActivity>() }
        } else {
            if (isOnline()) {
                WorkManager.getInstance(this).apply{
                    enqueueUniqueWork(
                        DATA_IMPORTED,
                        ExistingWorkPolicy.KEEP,
                        OneTimeWorkRequest.Companion.from(MoviesWorker::class.java)
                    )
                }
            } else {
                callDelayed(DELAY) { finish() }
            }
        }


    }


}