package com.example.besafe

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.besafe.databinding.AddAddrBinding
import android.content.Intent
import android.provider.ContactsContract
import android.util.Log


import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

import com.example.besafe.databinding.SettingLayoutBinding



class AddAddr : AppCompatActivity() {
    val binding by lazy {AddAddrBinding.inflate(layoutInflater)}
    val helper = SqliteHelper(this, "memo", 1)
    lateinit var requestLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)




        // ActivityResultLauncher 초기화, 결과 콜백 정의
        requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                Log.d("test", "Uri : ${it.data!!.data!!}")
                val cursor = contentResolver.query(
                    it.data!!.data!!,
                    arrayOf<String>(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ),
                    null,
                    null,
                    null
                )
                Log.d("test", "cursor size : ${cursor?.count}")

                if (cursor!!.moveToFirst()) {
                    val name = cursor.getString(0)
                    val phone = cursor.getString(1)

                    val adapter = RecyclerAdapter()
                    adapter.helper = helper
                    adapter.listData.addAll(helper.selectMemo())
                    binding.recyclerMemo.adapter =adapter
                    binding.recyclerMemo.layoutManager = LinearLayoutManager(this)

                        val memo = Memo(null, phone, System.currentTimeMillis())

                        helper.insertMemo(memo)
                        adapter.listData.clear()
                        adapter.listData.addAll(helper.selectMemo())
                        adapter.notifyDataSetChanged()
                        binding.editMemo.setText("")




                }
            }
        }
        binding.buttonSave.setOnClickListener {
            // 주소록 앱 연동
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
            requestLauncher.launch(intent)
        }


    }
}