package com.example.besafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.besafe.databinding.AddAddrBinding
import com.example.besafe.databinding.SettingLayoutBinding

class SettingActivity : AppCompatActivity() {
    val binding by lazy{ SettingLayoutBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val intent = Intent(this,AddAddr::class.java)
        val intent1 = Intent(this,WaytoguideActivity::class.java)

        binding.btn4.setOnClickListener{startActivity(intent)} // 비상연락망 설정
        binding.btn5.setOnClickListener{startActivity(intent1)} // 길안내방식

    }
}
