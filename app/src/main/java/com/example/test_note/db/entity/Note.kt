package com.example.test_note.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey
    @ColumnInfo(name = "note_id")
    val noteId: Long,
    @ColumnInfo(name = "note_title")
    val noteTitle: String,
    @ColumnInfo(name = "note_decryption")
    val noteDecryption: String,
    @ColumnInfo(name = "isEditable")
    val isEditable: Boolean
)
