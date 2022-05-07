package com.example.besafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class WaytoguideActivity : AppCompatActivity() {
    var total = 0
    var second = 0
    var minute = 0
    var hour = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.waytoguide_layout)

        val secondET = findViewById<TextView>(R.id.secondedit)
        val minuteET = findViewById<TextView>(R.id.minuteedit)
        val hourET = findViewById<TextView>(R.id.houredit)
        val btnstart = findViewById<Button>(R.id.startBtn2)
        val btnstop = findViewById<Button>(R.id.endBtn)
        val intent = Intent(this,guidemapActivity::class.java)//지도안내 액티비티

        //시작버튼 리스너
        btnstart.setOnClickListener {
            if(secondET.text.toString() != ""){
                second = secondET.text.toString().toInt()
            }
            if(minuteET.text.toString() != ""){
                minute = minuteET.text.toString().toInt()
            }
            if(hourET.text.toString() != ""){
                hour = hourET.text.toString().toInt()
            }
            total = hour*3600 + minute*60 + second
            intent.putExtra("total",total)
            startActivity(intent)
        }
        //초기화버튼 리스너
        btnstop.setOnClickListener { stop() }
    }
    //초기화버튼
    fun stop(){
        val secondET = findViewById<TextView>(R.id.secondedit)
        val minuteET = findViewById<TextView>(R.id.minuteedit)
        val hourET = findViewById<TextView>(R.id.houredit)

        hour=0
        minute=0
        second=0

        secondET.text = ""
        minuteET.text = ""
        hourET.text = ""
    }
}