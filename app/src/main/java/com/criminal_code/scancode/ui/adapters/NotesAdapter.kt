package com.criminal_code.scancode.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.criminal_code.scancode.R
import com.criminal_code.scancode.mvp.moxy.models.Note
import com.criminal_code.scancode.utils.formatDate

class NotesAdapter(private val notesList: List<Note>) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): NotesAdapter.ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.note_item_layout, viewGroup, false)
        return NotesAdapter.ViewHolder(v)
    }

    override
    fun onBindViewHolder(viewHolder: NotesAdapter.ViewHolder, i: Int) {
        val note = notesList[i]
        viewHolder.noteTitle.text = note.title
        viewHolder.noteDate.text = formatDate(note.createAt)
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var noteTitle: TextView = itemView.findViewById(R.id.tvItemNoteTitle) as TextView
        var noteDate: TextView = itemView.findViewById(R.id.tvItemNoteDate) as TextView

    }

}
