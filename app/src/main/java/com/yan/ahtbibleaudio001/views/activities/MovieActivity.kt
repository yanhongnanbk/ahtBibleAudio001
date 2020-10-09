package com.yan.ahtbibleaudio001.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.yan.ahtbibleaudio001.R

import com.yan.ahtbibleaudio001.views.fragments.MovieFragment


class MovieActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val movieFragment = MovieFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_movie_content_id, movieFragment).commit()
    }


}

