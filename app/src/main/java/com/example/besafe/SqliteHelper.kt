package com.example.besafe
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteHelper(context: Context, name: String, version: Int):
    SQLiteOpenHelper(context, name, null, version){
    var test ="000"


    override fun onCreate(db: SQLiteDatabase?) { //사용할 데이터베이스임
        val create = "create table memo (" +
                "no integer primary key, " +
                "content text, " +
                "datetime integer " +
                ")"

        db?.execSQL(create)
    }
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    fun insertMemo(memo: Memo){ // 삽입 메서드
        val values = ContentValues()
        values.put("content", memo.content)
        values.put("datetime",memo.datetime)

        val wd = writableDatabase
        wd.insert("memo", null, values)
        wd.close()
    }

    fun selectMemo(): MutableList<Memo>{ // 조회 메소드
        val list = mutableListOf<Memo>() // list 는 반환값임
        val select = "select * from memo"
        val rd = readableDatabase
        val cursor = rd.rawQuery(select, null)



        while (cursor.moveToNext()){   // 커서는 현재 위치를 포함하는 데이터 요소
            val noIdx = cursor.getColumnIndex("no")
            val contentIdx = cursor.getColumnIndex("content")
            val dateIdx = cursor.getColumnIndex("datetime")

            val no = cursor.getLong(noIdx)
            val content = cursor.getString(contentIdx)
            val datetime = cursor.getLong(dateIdx)

            test = cursor.getString(contentIdx)

            list.add(Memo(no, content, datetime))
        }


        cursor.close()
        rd.close()
        return list
    }
    fun updateMemo(memo: Memo){  // 수정 메서드
        val values = ContentValues()
        values.put("content", memo.content)
        values.put("datetime", memo.datetime)

        val wd = writableDatabase
        wd.update("memo", values, "no = $(memo.no}",null)
        wd.close()
    }

    fun deleteMemo(memo: Memo){ // 삭제 메서드
        val delete = "delete from memo where no = ${memo.no}"
        val db = writableDatabase
        db.execSQL(delete)
        db.close()

    }
}

data class Memo(var no: Long?, var content: String, var datetime: Long)