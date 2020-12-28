package com.example.test_note.ui.activity

import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.test_note.R
import com.example.test_note.db.AppDatabase
import com.example.test_note.db.dao.NoteDao
import com.example.test_note.db.entity.Note
import com.example.test_note.ui.fragments.ListNoteFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val ERROR_TAG = "ERROR"

class MainActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar

    companion object {
        const val LIST_NOTE_FRAGMENT_TAG = "listNoteFragmentTag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        getData()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        toolbar.navigationIcon = null
        toolbar.title = getString(R.string.app_title)
    }

    private fun getData() {
        GlobalScope.launch(Dispatchers.Main) {
            val listNote = withContext(Dispatchers.IO) {
                val noteDao: NoteDao = AppDatabase.getDatabase(applicationContext).noteDao()
                noteDao.getAll()
            }
            addListFragment(listNote)
        }
    }

    private fun addListFragment(listNote: List<Note>) {
        val fragment = ListNoteFragment.newInstance(listNote)

        try {
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(
                    R.id.fragment_container,
                    fragment,
                    LIST_NOTE_FRAGMENT_TAG
                )
                .disallowAddToBackStack()
                .commit()
        } catch (illExc: IllegalArgumentException) {
            Log.e(ERROR_TAG, illExc.toString())
        } catch (nullExc: NullPointerException) {
            Log.e(ERROR_TAG, nullExc.toString())
        }
    }
}