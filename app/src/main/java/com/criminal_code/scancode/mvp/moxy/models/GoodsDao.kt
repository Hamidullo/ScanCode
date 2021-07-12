package com.criminal_code.scancode.mvp.moxy.models

import com.reactiveandroid.query.Delete
import com.reactiveandroid.query.Select
import java.util.*


class GoodsDao {

    fun createGoods(noteId: Long): Goods {
        val goods = Goods("New goods", Date(), noteId)
        goods.save()
        return goods
    }

    fun saveGoods(goods: Goods): Long = goods.save()

    fun loadAllGoods(noteId: Long): MutableList<Goods> = Select.from(Goods::class.java).where("noteId = ?",noteId).fetch()

    fun getGoodsById(goodsId: Long): Goods? = Select.from(Goods::class.java).where("id = ?", goodsId).fetchSingle()

    fun deleteAllGoods() = Delete.from(Goods::class.java).execute()

    fun deleteGoods(goods: Goods) = goods.delete()
}