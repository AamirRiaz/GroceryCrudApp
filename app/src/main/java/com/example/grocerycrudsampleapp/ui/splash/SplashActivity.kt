package com.example.grocerycrudsampleapp.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.grocerycrudsampleapp.ui.main.MainActivity

class SplashActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val myIntent = Intent(this, MainActivity::class.java)
        startActivity(myIntent)
        finish()

    }
}