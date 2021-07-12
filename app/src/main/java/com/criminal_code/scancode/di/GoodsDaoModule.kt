package com.criminal_code.scancode.di

import com.criminal_code.scancode.mvp.moxy.models.GoodsDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class GoodsDaoModule {

    @Provides
    @Singleton
    fun provideGoodsDao(): GoodsDao = GoodsDao()
}