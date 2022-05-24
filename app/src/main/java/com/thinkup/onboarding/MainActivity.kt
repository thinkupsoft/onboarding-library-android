package com.thinkup.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thinkup.onboarding.databinding.ActivityMainBinding
import com.thinkup.onboarding.databinding.ActivityViewBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.composeButton.setOnClickListener {
            val intent = Intent(this,ComposeActivity::class.java)
            startActivity(intent)
        }
        binding.viewButton.setOnClickListener {
            val intent = Intent(this,ViewActivity::class.java)
            startActivity(intent)
        }
    }
}