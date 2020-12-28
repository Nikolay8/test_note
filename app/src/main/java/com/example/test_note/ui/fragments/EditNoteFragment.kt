package com.example.test_note.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.test_note.R
import com.example.test_note.db.AppDatabase
import com.example.test_note.db.dao.NoteDao
import com.example.test_note.db.entity.Note
import com.example.test_note.ui.activity.MainActivity
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class EditNoteFragment : Fragment() {

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var titleEditText: EditText
    private lateinit var titleTextInputLayout: TextInputLayout
    private lateinit var descriptionEditText: EditText
    private lateinit var saveNote: TextView
    private lateinit var deleteNote: TextView

    private lateinit var mainActivity: MainActivity

    private var selectedNoteId: Long? = null

    companion object {

        const val SELECTED_NOTE = "selectedNote"

        fun newInstance(
            noteId: Long?,
        ): EditNoteFragment {
            return EditNoteFragment().apply {
                arguments = bundleOf(SELECTED_NOTE to noteId)

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity
        selectedNoteId = requireArguments().getLong(SELECTED_NOTE)

        initViews()
    }

    private fun initViews() {
        toolbar = mainActivity.toolbar
        titleEditText = view!!.findViewById(R.id.fen_title_edit_text)
        titleTextInputLayout = view!!.findViewById(R.id.fen_title_input_layout)

        descriptionEditText = view!!.findViewById(R.id.fen_description_edit_text)
        saveNote = view!!.findViewById(R.id.save_note_text_view)
        deleteNote = view!!.findViewById(R.id.delete_note_text_view)

        if (selectedNoteId == 0L) {
            toolbar.title = getString(R.string.create_note)
            deleteNote.visibility = View.GONE
            saveNote.text = getString(R.string.save_note)
        } else {
            toolbar.title = getString(R.string.edit_note)
            deleteNote.visibility = View.VISIBLE
            saveNote.text = getString(R.string.edit_note)
            restoreNoteData()
        }

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)

        toolbar.setNavigationOnClickListener {
            toolbar.navigationIcon = null
            toolbar.title = getString(R.string.app_title)
            mainActivity.onBackPressed()
        }

        saveNote.setOnClickListener {
            saveImplementation()
        }
        deleteNote.setOnClickListener {
            deleteImplementation()
        }
    }

    private fun restoreNoteData() {
        GlobalScope.launch(Dispatchers.Main) {
            val insertedNote = withContext(Dispatchers.IO) {
                val noteDao: NoteDao =
                    AppDatabase.getDatabase(activity?.applicationContext!!).noteDao()
                noteDao.findById(selectedNoteId!!)
            }
            titleEditText.setText(insertedNote.noteTitle)
            descriptionEditText.setText(insertedNote.noteDecryption)
        }
    }

    private fun saveImplementation() {
        if (selectedNoteId != 0L) {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    val noteDao: NoteDao =
                        AppDatabase.getDatabase(activity?.applicationContext!!).noteDao()

                    val title = titleEditText.text.toString()
                    val descriptionEditText = descriptionEditText.text.toString()

                    val note = Note(selectedNoteId!!, title, descriptionEditText, true)
                    noteDao.update(note)
                }
                onCloseFragment()
            }
        } else {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    val noteDao: NoteDao =
                        AppDatabase.getDatabase(activity?.applicationContext!!).noteDao()
                    val calendar = Calendar.getInstance()
                    val title = titleEditText.text.toString()
                    val descriptionEditText = descriptionEditText.text.toString()

                    val note = Note(calendar.timeInMillis, title, descriptionEditText, false)
                    noteDao.insertNote(note)
                }
                onCloseFragment()
            }
        }
    }

    private fun deleteImplementation() {
        if (selectedNoteId != 0L) {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    val noteDao: NoteDao =
                        AppDatabase.getDatabase(activity?.applicationContext!!).noteDao()

                    val title = titleEditText.text.toString()
                    val descriptionEditText = descriptionEditText.text.toString()

                    val note = Note(selectedNoteId!!, title, descriptionEditText, true)
                    noteDao.delete(note)
                }
                onCloseFragment()
            }
        }
    }

    private fun onCloseFragment() {
        toolbar.navigationIcon = null
        toolbar.title = getString(R.string.app_title)

        val fragment: ListNoteFragment? =
            fragmentManager?.findFragmentByTag(MainActivity.LIST_NOTE_FRAGMENT_TAG) as ListNoteFragment?
        fragment?.updateData()

        activity?.supportFragmentManager?.popBackStack()
    }
}