package com.textsdev.cleantodo.di

import android.content.Context
import androidx.room.Room
import com.textsdev.cleantodo.data.database.AppDatabase
import com.textsdev.cleantodo.data.repository.TaskRepositoryImpl
import com.textsdev.cleantodo.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providesTaskRepository(appDatabase: AppDatabase): TaskRepository =
        TaskRepositoryImpl(appDatabase.taskDao())
}