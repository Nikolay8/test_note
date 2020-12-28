package com.example.test_note.db.dao

import androidx.room.*
import com.example.test_note.db.entity.Note


@Dao
interface NoteDao {
    @Query("SELECT * FROM note_table")
    fun getAll(): List<Note>

    @Query("SELECT * FROM note_table WHERE note_id LIKE :noteId")
    fun findById(noteId: Long): Note

    @Insert
    fun insertNote(vararg note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)
}