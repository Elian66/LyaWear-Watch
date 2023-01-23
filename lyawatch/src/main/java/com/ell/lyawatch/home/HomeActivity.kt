package com.ell.lyawatch.home

import android.app.Activity
import android.os.Bundle
import com.ell.lyawatch.databinding.ActivityHomeBinding

class HomeActivity : Activity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}