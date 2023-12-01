package com.margarin.commonweather.ui.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.margarin.commonweather.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initMainFragment()
    }

    private fun initMainFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.holder, MainFragment.newInstance("Тюмень"))
            .addToBackStack(null)
            .commit()
    }
}