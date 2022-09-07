package com.swastik.shayariapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.swastik.shayariapp.databinding.ActivitySelectBinding

class SelectActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startSelect.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
        binding.loginSelect.setOnClickListener {

            startActivity(Intent(this,LoginActivity::class.java))
        }
    }
}