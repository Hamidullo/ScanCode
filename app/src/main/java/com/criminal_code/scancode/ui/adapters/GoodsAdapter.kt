package com.criminal_code.scancode.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.criminal_code.scancode.R
import com.criminal_code.scancode.mvp.moxy.models.Goods
import com.criminal_code.scancode.mvp.moxy.models.Note
import com.criminal_code.scancode.utils.formatDate

class GoodsAdapter (private val goodsList: List<Goods>) : RecyclerView.Adapter<GoodsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): GoodsAdapter.ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.note_item_layout, viewGroup, false)
        return GoodsAdapter.ViewHolder(v)
    }

    override
    fun onBindViewHolder(viewHolder: GoodsAdapter.ViewHolder, i: Int) {
        val goods = goodsList[i]
        viewHolder.noteTitle.text = goods.name
        viewHolder.noteDate.text = goods.barcode.toString() + " sum" //formatDate(goods.createAt)
    }

    override fun getItemCount(): Int {
        return goodsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var noteTitle: TextView = itemView.findViewById(R.id.tvItemNoteTitle) as TextView
        var noteDate: TextView = itemView.findViewById(R.id.tvItemNoteDate) as TextView

    }

}
