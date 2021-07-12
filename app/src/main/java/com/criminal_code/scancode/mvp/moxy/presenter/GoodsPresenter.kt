package com.criminal_code.scancode.mvp.moxy.presenter

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.criminal_code.scancode.app.MyApp
import com.criminal_code.scancode.events_bus_model.GoodsDeleteAction
import com.criminal_code.scancode.events_bus_model.GoodsEditAction
import com.criminal_code.scancode.mvp.moxy.models.Goods
import com.criminal_code.scancode.mvp.moxy.models.GoodsDao
import com.criminal_code.scancode.mvp.moxy.view.GoodsView
import com.criminal_code.scancode.utils.getNotesSortMethodName
import com.criminal_code.scancode.utils.setNotesSortMethod
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*
import javax.inject.Inject

@InjectViewState
class GoodsPresenter: MvpPresenter<GoodsView>() {
    enum class SortGoodsBy : Comparator<Goods> {
        DATE {
            override fun compare(note1: Goods, note2: Goods) = note1.createAt!!.compareTo(note2.createAt)
        },
        NAME {
            override fun compare(note1: Goods, note2: Goods) = note1.name!!.compareTo(note2.name!!)
        },
    }

    @Inject
    lateinit var goodsDao: GoodsDao
    private lateinit var goodsList: MutableList<Goods>

    init {
        MyApp.graph.inject(this)
        EventBus.getDefault().register(this)
    }


    fun deleteAllNotes() {
        goodsDao.deleteAllGoods()
        goodsList.removeAll(goodsList)
        viewState.onAllNotesDeleted()
    }

    fun deleteNoteByPosition(position: Int) {
        val goods = goodsList[position]
        goodsDao.deleteGoods(goods)
        goodsList.remove(goods)
        viewState.onNoteDeleted()
    }

    fun openNote(position: Int) {
        viewState.openNoteScreen(goodsList[position].id)
    }

    @Subscribe
    fun onNoteEdit(action: GoodsEditAction) {
        val goodsPosition = getNotePositionById(action.goodsId)
        goodsList[goodsPosition] = goodsDao.getGoodsById(action.goodsId)!!
        sortNotesBy(getCurrentSortMethod())
    }

    fun openNewNote(noteId: Long) {
        val newGoods = goodsDao.createGoods(noteId)
        println(newGoods.id)
        println(newGoods.name)
        println(newGoods.createAt)
        goodsList.add(newGoods)
        sortNotesBy(getCurrentSortMethod())
        viewState.openNoteScreen(newGoods.id)
    }

    @Subscribe
    fun onNoteDelete(action: GoodsDeleteAction) {
        Log.d("Notelin", "onDeleted" + action.goodsId);
        val goodsPosition = getNotePositionById(action.goodsId)
        goodsList.removeAt(goodsPosition)
        viewState.updateView()
    }

    fun showNoteContextDialog(position: Int) {
        viewState.showNoteContextDialog(position)
    }

    fun hideNoteContextDialog() {
        viewState.hideNoteContextDialog()
    }

    fun showNoteDeleteDialog(position: Int) {
        viewState.showNoteDeleteDialog(position)
    }

    fun hideNoteDeleteDialog() {
        viewState.hideNoteDeleteDialog()
    }

    fun showNoteInfo(position: Int) {
        viewState.showNoteInfoDialog(goodsList[position].getInfo())
    }

    fun hideNoteInfoDialog() {
        viewState.hideNoteInfoDialog()
    }

    fun loadAllGoods(noteId: Long) {
        goodsList = goodsDao.loadAllGoods(noteId)
        for (i in goodsList){
            println(i.id)
            println(i.createAt)
            println(i.name)
        }
        Collections.sort(goodsList, getCurrentSortMethod())
        viewState.onNotesLoaded(goodsList)
    }

    fun search(query: String) {
        if (query == "") {
            viewState.onSearchResult(goodsList)
        } else {
            val searchResults = goodsList.filter { it.name!!.startsWith(query, ignoreCase = true) }
            viewState.onSearchResult(searchResults)
        }
    }

    fun sortNotesBy(sortMethod: SortGoodsBy) {
        goodsList.sortWith(sortMethod)
        setNotesSortMethod(sortMethod.toString())
        viewState.updateView()
    }

    private fun getCurrentSortMethod(): SortGoodsBy {
        val defaultSortMethodName = SortGoodsBy.DATE.toString()
        val currentSortMethodName = getNotesSortMethodName(defaultSortMethodName)
        return SortGoodsBy.valueOf(currentSortMethodName)
    }

    private fun getNotePositionById(noteId: Long) = goodsList.indexOfFirst { it.id == noteId }
}