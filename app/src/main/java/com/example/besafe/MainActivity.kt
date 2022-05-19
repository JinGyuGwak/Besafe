package com.example.besafe

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.besafe.databinding.ActivityMainBinding
import java.lang.Exception
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import android.content.Intent as Intent

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()

        val keyHash = com.kakao.util.maps.helper.Utility.getKeyHash(this /* context */)
        Log.d("Hash",keyHash)

        //getHashKey()

        val btn1 = findViewById<Button>(R.id.btn1) //안전귀가 버튼
        val btn2 = findViewById<Button>(R.id.btn2) //설정 버튼

        btn2.setOnClickListener { //설정 버튼
            val intent = Intent(this, AddAddr::class.java) // 수정수정***************************
            startActivity(intent)
        }

        btn1.setOnClickListener { //안전귀가 버튼
            val intent = Intent(this, WaytoguideActivity::class.java)
            startActivity(intent)
        }
    }
    //카카오 해시키
    private fun getHashKey() {
        var packageInfo: PackageInfo? = null
        try {
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (packageInfo == null) Log.e("KeyHash", "KeyHash:null")
        for (signature in packageInfo!!.signatures) {
            try {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            } catch (e: NoSuchAlgorithmException) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=$signature", e)
            }
        }
    }
    //권한확인
    fun checkPermission() {
        val locationPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
        val contactPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions()
        }
        if (contactPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions()
        }
    }
    //권한요청
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.READ_CONTACTS), 99)
    }
}