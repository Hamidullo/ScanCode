package com.criminal_code.scancode.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.criminal_code.scancode.events_bus_model.GoodsEditAction
import com.criminal_code.scancode.mvp.moxy.models.BDHandler
import com.criminal_code.scancode.mvp.moxy.models.Goods
import com.criminal_code.scancode.mvp.moxy.models.GoodsDao
import com.google.zxing.Result
import com.pawegio.kandroid.i
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.greenrobot.eventbus.EventBus
import java.util.*
import javax.inject.Inject

class ScanCodeActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private val STORAGE_PERMISSION_CODE = 1

    @Inject
    lateinit var goodsDao: GoodsDao
    private lateinit var goods: Goods

    private lateinit var ScannerView: ZXingScannerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ScannerView = ZXingScannerView(this)
        setContentView(ScannerView)

        if (ContextCompat.checkSelfPermission(applicationContext,
                android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED){
            requestPermission()
        }
    }
    private fun requestPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.CAMERA)){
            AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("This permission is needed because of this and that")
                .setPositiveButton("OK",DialogInterface.OnClickListener { _: DialogInterface, i: Int ->
                    ActivityCompat.requestPermissions(this,
                        listOf<String>(android.Manifest.permission.CAMERA).toTypedArray(),STORAGE_PERMISSION_CODE) })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialog, which -> dialog.dismiss() })
                .create().show()
        } else{
            ActivityCompat.requestPermissions(this,
                listOf<String>(android.Manifest.permission.CAMERA).toTypedArray(),STORAGE_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    override fun handleResult(p0: Result?) {
        val db = BDHandler.getDatabase()
        val list = db!!.readUser(p0.toString())
        if (list.isEmpty()){
            Toast.makeText(this,"Товар не существует на базе данных!",Toast.LENGTH_SHORT).show()
            goodsDao = GoodsDao()
            goods = goodsDao.getGoodsById(intent.getLongExtra("goods_id",0))!!
            goodsDao.deleteGoods(goods)
            startActivity(GoodsActivity.buildIntent(this, intent.getLongExtra("id",goods.id)))
            onBackPressed()

        } else {
            goodsDao = GoodsDao()
            goods = goodsDao.getGoodsById(intent.getLongExtra("goods_id",0))!!
            goods.name = list[0].NAME
            goods.name1 = list[0].TEXT
            goods.barcode = list[0].COST
            goods.createAt = Date()
            goods.noteId = intent.getLongExtra("id",goods.id)

            goodsDao.saveGoods(goods)
            startActivity(GoodsActivity.buildIntent(this, intent.getLongExtra("id",goods.id)))
            onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
        ScannerView.stopCamera()
    }

    override fun onResume() {
        super.onResume()
        ScannerView.setResultHandler(this)
        ScannerView.startCamera()
    }
}