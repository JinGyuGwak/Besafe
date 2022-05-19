package com.example.besafe

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.addr_recycler.*
import net.daum.mf.map.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


import kotlin.concurrent.thread


class guidemapActivity : AppCompatActivity(){
    var started = false
    var total=0
    val helper = SqliteHelper(this, "memo", 1)
    private var list = arrayListOf<Place>()
    private var markerArr = arrayListOf<MapPOIItem>()
    companion object {
        const val BASE_URL = "https://dapi.kakao.com/"
        const val API_KEY = "KakaoAK 607ec9bb5b4778084d650f4a705a78e9"  // REST API 키
    }
    //수정 된거
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guidemap_layout)

        val stopbtn = findViewById<Button>(R.id.Stopbtn)

        total = intent.getIntExtra("total", 0)
        val adapter = RecyclerAdapter()
        adapter.helper = helper
        adapter.listData.addAll(helper.selectMemo())
        start()

        val mapView = MapView(this)
        val mapViewContainer = findViewById<View>(R.id.guidemap_view) as ViewGroup
        mapViewContainer.addView(mapView)
        mapView.mapViewEventListener
        //mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading)

        val btn_concenience = findViewById<Button>(R.id.btn_concenience) //편의점 표시 버튼
        val btn_police = findViewById<Button>(R.id.btn_police) //편의점 표시 버튼

        val circle1 = MapCircle(    //범죄 장소 원그리기
            MapPoint.mapPointWithGeoCoord(37.3766, 126.6378),  // 범죄발생지역
            50,  // radius
            Color.argb(128, 255, 0, 0),  // strokeColor
            Color.argb(128, 0, 255, 0) // fillColor
        )
        circle1.tag = 1234
        mapView.addCircle(circle1)
        val circle2 = MapCircle(    //범죄 장소 원그리기
            MapPoint.mapPointWithGeoCoord(37.3793, 126.6375),  // 범죄발생지역
            50,  // radius
            Color.argb(128, 255, 0, 0), // strokeColor
            Color.argb(128, 255, 255, 0) // fillColor
        )
        circle2.tag = 5678
        mapView.addCircle(circle2)

// 지도뷰의 중심좌표와 줌레벨을 Circle이 모두 나오도록 조정.
        val mapPointBoundsArray = arrayOf(circle1.bound, circle2.bound)
        val mapPointBounds = MapPointBounds(mapPointBoundsArray)
        val padding = 50 // px

        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding))

        btn_concenience.setOnClickListener { //편의점 클릭

            val keyword = "송도동 편의점"
            val retrofit = Retrofit.Builder()   // Retrofit 구성
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val api = retrofit.create(KakaoAPI::class.java)   // 통신 인터페이스를 객체로 생성
            val call = api.getSearchKeyword(API_KEY, keyword)   // 검색 조건 입력

            // API 서버에 요청
            call.enqueue(object: Callback<ResultSearchKeyword> {
                override fun onResponse(
                    call: Call<ResultSearchKeyword>,
                    response: Response<ResultSearchKeyword>
                ) {
                    // 통신 성공 (검색 결과는 response.body()에 담겨있음)
                    val re = response.body()

                    Log.d("Test", "Raw: ${response.raw()}")
                    Log.d("Test", "Body: ${response.body()}")
                    //Log.d("Test", "Code: ${response.body()!!.documents}")
                    list.addAll(response.body()!!.documents)
                    Log.d("Test", "함수: ${list.size}")

                    for (data in response.body()!!.documents) {
                        val marker = MapPOIItem()
                        marker.mapPoint = MapPoint.mapPointWithGeoCoord(data.y.toDouble(), data.x.toDouble())
                        marker.markerType = MapPOIItem.MarkerType.BluePin
                        marker.itemName = data.place_name
                        markerArr.add(marker)
                    }
                    mapView.addPOIItems(markerArr.toTypedArray())
                }

                override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                    // 통신 실패
                    Log.w("MainActivity", "통신 실패: ${t.message}")
                }
            })
        }

        btn_police.setOnClickListener{  //경찰서 클릭
            mapView.removeAllPOIItems()
            val keyword = "송도동 경찰서"
            val retrofit = Retrofit.Builder()   // Retrofit 구성
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            val api = retrofit.create(KakaoAPI::class.java)   // 통신 인터페이스를 객체로 생성
            val call = api.getSearchKeyword(API_KEY, keyword)   // 검색 조건 입력

            // API 서버에 요청
            call.enqueue(object: Callback<ResultSearchKeyword> {
                override fun onResponse(
                    call: Call<ResultSearchKeyword>,
                    response: Response<ResultSearchKeyword>
                ) {
                    // 통신 성공 (검색 결과는 response.body()에 담겨있음)
                    val re = response.body()

                    //Log.d("Test", "Raw: ${response.raw()}")
                    //Log.d("Test", "Body: ${response.body()}")
                    //Log.d("Test", "Code: ${response.body()!!.documents}")
                    list.addAll(response.body()!!.documents)
                    Log.d("Test", "함수: ${list.size}")

                    for (data in response.body()!!.documents) {
                        val marker = MapPOIItem()
                        marker.mapPoint = MapPoint.mapPointWithGeoCoord(data.y.toDouble(), data.x.toDouble())
                        marker.markerType = MapPOIItem.MarkerType.RedPin
                        marker.itemName = data.place_name
                        markerArr.add(marker)
                    }
                    mapView.addPOIItems(markerArr.toTypedArray())
                }

                override fun onFailure(call: Call<ResultSearchKeyword>, t: Throwable) {
                    // 통신 실패
                    Log.w("MainActivity", "통신 실패: ${t.message}")
                }
            })
        }
        stopbtn.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@guidemapActivity)
            builder.setMessage("목적지에 도착하셨습니까?")
            builder.setTitle("종료 알림창")
                .setCancelable(false)
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, i-> ActivityCompat.finishAffinity(this); System.exit(0); })
                .setNegativeButton("No",
                    DialogInterface.OnClickListener { dialog, i -> dialog.cancel() })
            val alert: AlertDialog = builder.create()
            alert.setTitle("종료 알림창")
            alert.show()
        }

    }



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
                        sendSMS(helper.test, "귀가 미완료") // 컴퓨터로 실행하면 여기서 팅김

                    }
                }
            }
        }
    }
    // 문자 전송 함수
    open fun sendSMS(phoneNumber: String?, message: String?) {
        val mysmsManager = SmsManager.getDefault()
        mysmsManager.sendTextMessage(helper.test,null, message, null, null) // 시간 종료되면 번호로 문자 전송
    }

}