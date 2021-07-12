package com.criminal_code.scancode.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.criminal_code.scancode.R
import com.criminal_code.scancode.mvp.moxy.models.Goods
import com.criminal_code.scancode.mvp.moxy.presenter.GoodsPresenter
import com.criminal_code.scancode.mvp.moxy.view.GoodsView
import com.criminal_code.scancode.ui.adapters.GoodsAdapter
import com.criminal_code.scancode.ui.commons.ItemClickSupport
import com.pawegio.kandroid.onQueryChange
import kotlinx.android.synthetic.main.activity_goods.*


class GoodsActivity : MvpAppCompatActivity(),GoodsView {

    companion object {
        const val NOTE_DELETE_ARG = "note_id"

        fun buildIntent(activity: Activity, noteId: Long) : Intent {
            val intent = Intent(activity, GoodsActivity::class.java)
            intent.putExtra(NOTE_DELETE_ARG, noteId)
            return intent
        }
    }

    @InjectPresenter
    lateinit var presenter: GoodsPresenter
    private var goodsContextDialog: MaterialDialog? = null
    private var goodsDeleteDialog: MaterialDialog? = null
    private var goodsInfoDialog: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods)

        setSupportActionBar(findViewById(R.id.toolbar))

        println(intent.getLongExtra("note_id",0))

        presenter.loadAllGoods(intent.getLongExtra("note_id",0))

        with(ItemClickSupport.addTo(goodsList)) {
            setOnItemLongClickListener { _, position, _ -> presenter.showNoteContextDialog(position); true }
        }

        newGoodsFab.setOnClickListener {
            presenter.openNewNote(intent.getLongExtra("note_id",0))
            finish()
        }
    }

    override fun onNotesLoaded(goods: List<Goods>) {
        goodsList.adapter = GoodsAdapter(goods)
        goodsList.layoutManager = LinearLayoutManager(this)
        updateView()
    }

    override fun onGoodsSaved() {
        updateView()
    }

    override fun updateView() {
        goodsList.adapter!!.notifyDataSetChanged()
        if (goodsList.adapter!!.itemCount == 0) {
            goodsList.visibility = View.GONE
            tvGoodsIsEmpty.visibility = View.VISIBLE
        } else {
            goodsList.visibility = View.VISIBLE
            tvGoodsIsEmpty.visibility = View.GONE
        }
    }

    override fun onNoteDeleted() {
        updateView()
        Toast.makeText(this, R.string.note_deleted, Toast.LENGTH_SHORT).show()
    }


    override fun onAllNotesDeleted() {
        updateView()
        Toast.makeText(this, R.string.all_notes_deleted, Toast.LENGTH_SHORT).show()
    }

    override fun onSearchResult(goods: List<Goods>) {
        goodsList.adapter = GoodsAdapter(goods)
    }

    override fun showNoteContextDialog(goodsPosition: Int) {
        goodsContextDialog = MaterialDialog.Builder(this)
            .items(R.array.main_note_context)
            .itemsCallback { _, _, position, _ ->
                onContextDialogItemClick(position, goodsPosition)
                presenter.hideNoteContextDialog()
            }
            .cancelListener { presenter.hideNoteContextDialog() }
            .show()
    }

    override fun hideNoteContextDialog() {
        goodsContextDialog?.dismiss()
    }


    override fun showNoteDeleteDialog(notePosition: Int) {
        goodsDeleteDialog = MaterialDialog.Builder(this)
            .title(getString(R.string.note_deletion_title))
            .content(getString(R.string.note_deletion_message))
            .positiveText(getString(R.string.yes))
            .negativeText(getString(R.string.no))
            .onPositive { _, _ ->
                presenter.deleteNoteByPosition(notePosition)
                goodsInfoDialog?.dismiss()
            }
            .onNegative { _, _ -> presenter.hideNoteDeleteDialog() }
            .cancelListener { presenter.hideNoteDeleteDialog() }
            .show()
    }

    override fun hideNoteDeleteDialog() {
        goodsDeleteDialog?.dismiss()
    }

    override fun showNoteInfoDialog(noteInfo: String) {
        goodsInfoDialog = MaterialDialog.Builder(this)
            .title(R.string.note_info)
            .positiveText(getString(R.string.ok))
            .content(noteInfo)
            .onPositive { materialDialog, dialogAction -> presenter.hideNoteInfoDialog() }
            .cancelListener { presenter.hideNoteInfoDialog() }
            .show()
    }

    override fun hideNoteInfoDialog() {
        goodsInfoDialog?.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        initSearchView(menu)
        return true
    }

    override fun openNoteScreen(noteId: Long) {
        startActivity(Intent(applicationContext, ScanCodeActivity::class.java)
                .putExtra("id",intent.getLongExtra("note_id",0))
                .putExtra("goods_id",noteId))
    }

    private fun initSearchView(menu: Menu) {
        val searchViewMenuItem = menu.findItem(R.id.action_search)
        val searchView = searchViewMenuItem.actionView as SearchView
        searchView.onQueryChange { query -> presenter.search(query) }
        searchView.setOnCloseListener { presenter.search(""); false }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuDeleteAllNotes -> presenter.deleteAllNotes()
            R.id.menuSortByName -> presenter.sortNotesBy(GoodsPresenter.SortGoodsBy.NAME)
            R.id.menuSortByDate -> presenter.sortNotesBy(GoodsPresenter.SortGoodsBy.DATE)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onContextDialogItemClick(contextItemPosition: Int, notePosition: Int) {
        when (contextItemPosition) {
            0 -> presenter.openNote(notePosition)
            1 -> presenter.showNoteInfo(notePosition)
            2 -> presenter.showNoteDeleteDialog(notePosition)
        }
    }
}