package com.criminal_code.scancode.mvp.moxy.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.criminal_code.scancode.mvp.moxy.models.Goods

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface GoodsView: MvpView {

    fun onNotesLoaded(goods: List<Goods>)

    fun updateView()

    fun onSearchResult(goods: List<Goods>)

    fun onGoodsSaved()

    fun onAllNotesDeleted()

    fun onNoteDeleted()

    fun showNoteInfoDialog(goodsInfo: String)

    fun hideNoteInfoDialog()

    fun showNoteDeleteDialog(goodsPosition: Int)

    fun hideNoteDeleteDialog()

    fun showNoteContextDialog(goodsPosition: Int)

    fun hideNoteContextDialog()

    fun openNoteScreen(goodsId: Long)
}