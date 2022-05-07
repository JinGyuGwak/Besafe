package com.example.besafe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.besafe.databinding.AddrRecyclerBinding
import java.text.SimpleDateFormat

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.Holder>() {
    var helper : SqliteHelper? = null
    var listData = mutableListOf<Memo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = AddrRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val memo = listData.get(position)
        holder.setMemo(memo)
    }//listData[0].textContent

    inner class Holder(val binding: AddrRecyclerBinding) : RecyclerView.ViewHolder(binding.root){
        var mMemo : Memo? = null
        init {
            binding.buttonDelete.setOnClickListener{
                helper?.deleteMemo(mMemo!!)
                listData.remove(mMemo)
                notifyDataSetChanged()

            }
        }
        fun setMemo(memo: Memo){
            binding.textContent.text=memo.content
            this.mMemo = memo
        }
    }
}
