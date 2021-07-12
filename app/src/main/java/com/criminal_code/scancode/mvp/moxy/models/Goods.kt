package com.criminal_code.scancode.mvp.moxy.models

import com.criminal_code.scancode.utils.formatDate
import com.reactiveandroid.Model
import com.reactiveandroid.annotation.Column
import com.reactiveandroid.annotation.PrimaryKey
import com.reactiveandroid.annotation.Table
import java.util.*

@Table(name = "Goods", database = AppDatabase::class)
class Goods : Model{

    @PrimaryKey
    var id: Long = 0
    @Column(name = "name")
    var name: String? = null
    @Column(name = "barcode")
    var barcode: Long? = null
    @Column(name = "name1")
    var name1: String? = null
    @Column(name = "created_at")
    var createAt: Date? = null
    @Column(name = "noteId")
    var noteId: Long? = null


    constructor(name: String, createDate: Date, noteId: Long) {
        this.name = name
        this.createAt = createDate
        this.noteId = noteId
    }

    constructor()

    fun getInfo(): String = "Goods :\n$name\n" +
            "Type :\n${name1}\n" +
            "Created at:\n${formatDate(createAt)}\n"

}