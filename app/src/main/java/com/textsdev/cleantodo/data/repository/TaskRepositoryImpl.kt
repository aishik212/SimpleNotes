package com.textsdev.cleantodo.data.repository

import androidx.lifecycle.LiveData
import com.textsdev.cleantodo.data.dao.TaskDao
import com.textsdev.cleantodo.domain.model.Task
import com.textsdev.cleantodo.domain.repository.TaskRepository

class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {

    override fun getAllTasks(): LiveData<List<Task>> {
        return taskDao.getAllTasks()
    }

    override suspend fun updateTask(task: Task) {
        taskDao.update(task)
    }

    override suspend fun addTask(task: Task) {
        taskDao.insert(task)
    }

    override suspend fun deleteTask(taskId: Long) {
        taskDao.delete(taskId)
    }
}