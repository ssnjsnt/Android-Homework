package com.example.clock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ClockActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock)
        val clockView:ClockView = findViewById(R.id.ClockView)
        clockView.startAnim()
    }
}