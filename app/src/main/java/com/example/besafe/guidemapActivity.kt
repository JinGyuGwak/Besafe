package com.example.besafe

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.concurrent.thread


class guidemapActivity : AppCompatActivity(){
    var started = false
    var total=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guidemap_layout)
        total = intent.getIntExtra("total", 0)
        start()
    }
    //타이머 작동
    fun start(){
        val secondEdit = findViewById<TextView>(R.id.secondedit)
        val minuteEdit = findViewById<TextView>(R.id.minuteedit)
        val hourEdit = findViewById<TextView>(R.id.houredit)
        var hour1 = 0
        var minute1 = 0
        var second1 = 0
        started = true
        thread(start=true){
            while(started){
                Thread.sleep(1000)
                total = total - 1
                runOnUiThread {
                    if(started){
                        hour1 = total / 3600
                        minute1 = (total % 3600) / 60
                        second1= total % 60
                        hourEdit.text = hour1.toString()
                        minuteEdit.text = minute1.toString()
                        secondEdit.text = second1.toString()
                    }
                    if(total==0){
                        started=false
                        Toast.makeText(this, "시간이 종료되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}