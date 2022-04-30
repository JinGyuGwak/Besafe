package com.example.besafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_layout)

        val btn3 = findViewById<Button>(R.id.btn3)

        btn3.setOnClickListener {
            val intent = Intent(this, addrActivity::class.java)
            startActivity(intent)
        }

    }
}