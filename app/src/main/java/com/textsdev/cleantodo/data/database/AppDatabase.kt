package com.textsdev.cleantodo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.textsdev.cleantodo.data.dao.TaskDao
import com.textsdev.cleantodo.domain.model.Task

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao
}