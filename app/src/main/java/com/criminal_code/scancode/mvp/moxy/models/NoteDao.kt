package com.criminal_code.scancode.mvp.moxy.models

import com.reactiveandroid.query.Delete
import com.reactiveandroid.query.Select
import java.util.*

class NoteDao {
    fun createNote(): Note {
        val note = Note("To day", Date()) //${Date().time}
        note.save()
        return note
    }

    fun saveNote(note: Note): Long = note.save()

    fun loadAllNotes(): MutableList<Note> = Select.from(Note::class.java).fetch()

    fun getNoteById(noteId: Long): Note? = Select.from(Note::class.java).where("id = ?", noteId).fetchSingle()

    fun deleteAllNotes() = Delete.from(Note::class.java).execute()

    fun deleteNote(note: Note) = note.delete()
}