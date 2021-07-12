package com.criminal_code.scancode.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.criminal_code.scancode.di.AppComponent
import com.criminal_code.scancode.di.DaggerAppComponent
import com.criminal_code.scancode.di.GoodsDaoModule
import com.criminal_code.scancode.di.NoteDaoModule
import com.criminal_code.scancode.mvp.moxy.models.AppDatabase
import com.criminal_code.scancode.mvp.moxy.models.BDHandler
import com.criminal_code.scancode.mvp.moxy.models.Goods
import com.criminal_code.scancode.mvp.moxy.models.Note
import com.reactiveandroid.ReActiveAndroid
import com.reactiveandroid.ReActiveConfig
import com.reactiveandroid.internal.database.DatabaseConfig

class MyApp: Application() {
    companion object {
        lateinit var graph: AppComponent

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()

        context = this

        BDHandler.init(context)

        graph = DaggerAppComponent.builder().noteDaoModule(NoteDaoModule()).goodsDaoModule(
            GoodsDaoModule()
        ).build()

        val appDatabaseConfig = DatabaseConfig.Builder(AppDatabase::class.java)
            .addModelClasses(Note::class.java).addModelClasses(Goods::class.java)
            .build()

        ReActiveAndroid.init(
            ReActiveConfig.Builder(this)
            .addDatabaseConfigs(appDatabaseConfig)
            .build())
    }
}