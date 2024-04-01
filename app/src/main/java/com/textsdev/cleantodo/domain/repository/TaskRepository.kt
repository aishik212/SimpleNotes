package com.textsdev.cleantodo.domain.repository

import androidx.lifecycle.LiveData
import com.textsdev.cleantodo.domain.model.Task

interface TaskRepository {
    fun getAllTasks(): LiveData<List<Task>>
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(taskId: Long)
}