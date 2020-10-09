package com.yan.ahtbibleaudio001.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.yan.ahtbibleaudio001.R

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            //Your Code
            startActivity(Intent(this, NavigationDemoActivity::class.java))
        }, 2000)
        finish()

        Log.d("XYX","sfsfd")
    }
}
