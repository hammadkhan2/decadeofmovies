package com.swvl.adecadeofmovies.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.swvl.adecadeofmovies.databinding.ActivitySplashBinding
import com.swvl.adecadeofmovies.utils.Constants.SPLASH_DELAY_DURATION
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        GlobalScope.launch {
            delay(SPLASH_DELAY_DURATION)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}