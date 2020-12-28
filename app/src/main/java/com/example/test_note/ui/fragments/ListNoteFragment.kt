package com.example.test_note.ui.fragments

import android.R.attr.rating
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_note.R
import com.example.test_note.db.AppDatabase
import com.example.test_note.db.dao.NoteDao
import com.example.test_note.db.entity.Note
import com.example.test_note.ui.activity.ERROR_TAG
import com.example.test_note.ui.activity.MainActivity
import com.example.test_note.ui.adapter.ListNoteAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val LIST_NOTE = "listNote"

class ListNoteFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noNoteInfoTextView: TextView
    private lateinit var addNewNoteTextView: TextView

    private var listNote: List<Note>? = null
    private var listAdapter: ListNoteAdapter? = null

    private lateinit var mainActivity: MainActivity

    companion object {
        fun newInstance(
            listNote: List<Note>
        ): ListNoteFragment {
            return ListNoteFragment().apply {
                arguments = bundleOf(LIST_NOTE to listNote)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity
        listNote = requireArguments().getSerializable(LIST_NOTE) as List<Note>

        initViews()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        listNote=savedInstanceState?.getSerializable(LIST_NOTE) as? List<Note>
    }

    private fun initViews() {
        recyclerView = view!!.findViewById(R.id.fln_recyclerView)
        noNoteInfoTextView = view!!.findViewById(R.id.fln_no_note_info_text_view)
        addNewNoteTextView = view!!.findViewById(R.id.fln_add_new_note_button)

        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        if (listNote.isNullOrEmpty()) {
            noNoteInfoTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            noNoteInfoTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            updateAdapter()
        }

        addNewNoteTextView.setOnClickListener {
            addEditFragment(null)
        }
    }

    private fun updateAdapter() {
        if (listNote.isNullOrEmpty()) {
            noNoteInfoTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            noNoteInfoTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        listAdapter = ListNoteAdapter(listNote!!, object : ListNoteAdapter.OnItemClickCallback {
            override fun onItemClick(noteId: Long) {
                addEditFragment(noteId)
            }

        })
        recyclerView.adapter = listAdapter
    }

    private fun addEditFragment(noteId: Long?) {
        val fragment = EditNoteFragment.newInstance(noteId)

        try {
            activity?.supportFragmentManager
                ?.beginTransaction()
                ?.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                ?.add(
                    R.id.fragment_container,
                    fragment,
                    fragment.javaClass.name
                )
                ?.addToBackStack(fragment.javaClass.name)
                ?.commit()
        } catch (illExc: IllegalArgumentException) {
            Log.e(ERROR_TAG, illExc.toString())
        } catch (nullExc: NullPointerException) {
            Log.e(ERROR_TAG, nullExc.toString())
        }
    }

    fun updateData() {
        GlobalScope.launch(Dispatchers.Main) {
            listNote = withContext(Dispatchers.IO) {
                val noteDao: NoteDao =
                    AppDatabase.getDatabase(activity?.applicationContext!!).noteDao()
                noteDao.getAll()
            }
            updateAdapter()
        }
    }
}