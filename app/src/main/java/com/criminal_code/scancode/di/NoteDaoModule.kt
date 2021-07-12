package com.criminal_code.scancode.di

import com.criminal_code.scancode.mvp.moxy.models.NoteDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NoteDaoModule {

    @Provides
    @Singleton
    fun provideNoteDao(): NoteDao = NoteDao()

}