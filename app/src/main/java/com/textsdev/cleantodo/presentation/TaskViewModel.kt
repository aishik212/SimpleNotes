package com.textsdev.cleantodo.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.textsdev.cleantodo.domain.model.Task
import com.textsdev.cleantodo.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskUIState(
    val isAddTaskDialogVisible: Boolean = false,
    val error: String = "",
    val isLoadingVisible: Boolean = false,
    val noteValue: String = ""
)

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskRepository: TaskRepository) : ViewModel() {
    private val _tasks: LiveData<List<Task>> = taskRepository.getAllTasks()
    val tasks: LiveData<List<Task>> get() = _tasks

    private val _uiState = MutableStateFlow(TaskUIState())
    val uiState: StateFlow<TaskUIState> = _uiState

    // Function to fetch tasks from the repository

    fun showAddTaskDialog() {
        _uiState.value = _uiState.value.copy(isAddTaskDialogVisible = true)
    }

    fun showLoadingDialog() {
        _uiState.value = _uiState.value.copy(isLoadingVisible = true)
    }

    fun hideLoadingDialog() {
        _uiState.value = _uiState.value.copy(isLoadingVisible = false)
    }

    private fun showErrorDialog(e: String) {
        _uiState.value = _uiState.value.copy(error = e)
    }

    private fun hideErrorDialog(e: String) {
        _uiState.value = _uiState.value.copy(error = "")
    }

    fun hideAddTaskDialog() {
        _uiState.value = _uiState.value.copy(isAddTaskDialogVisible = false)
    }

    fun setNoteValue(it: String) {
        _uiState.value = _uiState.value.copy(noteValue = it)
    }

    fun AddNote() {
        viewModelScope.launch {
            try {
                taskRepository.addTask(
                    Task(
                        title = uiState.value.noteValue.take(20),
                        description = uiState.value.noteValue
                    )
                )
                hideLoadingDialog()
                hideAddTaskDialog()
                setNoteValue("")
            } catch (e: Exception) {
                hideLoadingDialog()
                showErrorDialog(e.localizedMessage ?: "null")
            }
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch {
            try {
                taskRepository.deleteTask(id)
                hideLoadingDialog()
                setNoteValue("")
            } catch (e: Exception) {
                hideLoadingDialog()
                showErrorDialog(e.localizedMessage ?: "null")
            }
        }
    }


}