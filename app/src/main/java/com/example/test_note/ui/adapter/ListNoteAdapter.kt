package com.example.test_note.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.test_note.R
import com.example.test_note.db.entity.Note
import com.example.test_note.utils.DateHelper

class ListNoteAdapter(
    private val noteList: List<Note>,
    private val onItemClickCallback: OnItemClickCallback
) :
    RecyclerView.Adapter<ListNoteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = noteList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listItem = noteList[position]

        holder.dateTextView.text = DateHelper().getStringFromDate(listItem.noteId)
        holder.titleTextView.text = listItem.noteTitle
        holder.descriptionTextView.text = listItem.noteDecryption

        val editInfoStatus = if (listItem.isEditable) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
        holder.editInfoTextView.visibility = editInfoStatus

        holder.mainLayout.setOnClickListener {
            onItemClickCallback.onItemClick(listItem.noteId)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mainLayout: LinearLayout = view.findViewById(R.id.item_main_layout)
        val dateTextView: TextView = view.findViewById(R.id.item_created_time_text_view)
        val titleTextView: TextView = view.findViewById(R.id.item_title_text_view)
        val descriptionTextView: TextView = view.findViewById(R.id.item_desc_text_view)
        val editInfoTextView: TextView = view.findViewById(R.id.item_edit_info_text_view)
    }

    interface OnItemClickCallback {
        fun onItemClick(noteId: Long)
    }
}