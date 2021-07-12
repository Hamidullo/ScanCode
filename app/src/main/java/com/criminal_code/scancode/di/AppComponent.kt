package com.criminal_code.scancode.di

import com.criminal_code.scancode.mvp.moxy.presenter.GoodsPresenter
import com.criminal_code.scancode.mvp.moxy.presenter.MainPresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NoteDaoModule::class, GoodsDaoModule::class])
interface AppComponent {
    fun inject(mainPresenter: MainPresenter)

    fun inject(goodsPresenter: GoodsPresenter)
}