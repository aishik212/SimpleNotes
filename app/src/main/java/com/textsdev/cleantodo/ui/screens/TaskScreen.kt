package com.textsdev.cleantodo.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.textsdev.cleantodo.R
import com.textsdev.cleantodo.domain.model.Task
import com.textsdev.cleantodo.presentation.TaskViewModel

@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.observeAsState(initial = emptyList())
    val uiState by viewModel.uiState.collectAsState()
    Column {
        TaskList(
            tasks = tasks,
            Modifier
                .fillMaxSize(1F)
                .weight(0.8F),
            viewModel
        )
        TaskAddButton(Modifier.weight(0.2F)) {
            viewModel.showAddTaskDialog()
        }
    }

    if (uiState.isAddTaskDialogVisible) {
        Dialog(onDismissRequest = { viewModel.hideAddTaskDialog() }) {
            Card {
                Column(Modifier.padding(6.dp)) {
                    Text("Write Note")
                    TextField(value = uiState.noteValue,
                        onValueChange = { viewModel.setNoteValue(it) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle.Default.copy(textAlign = TextAlign.Start),
                        maxLines = 5,
                        label = { Text("Your Note") })
                    Row(
                        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { viewModel.hideAddTaskDialog() }) {
                            Text("Cancel")
                        }
                        TextButton(onClick = {
                            viewModel.AddNote()
                            viewModel.hideAddTaskDialog()
                        }) {
                            Text("Add")
                        }
                    }
                }
            }
        }
    }
    if (uiState.isLoadingVisible) {
        Box(
            Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.transparent_black))
        ) {
            CircularProgressIndicator(
                Modifier
                    .wrapContentSize()
                    .align(Alignment.Center)
                    .size(48.dp)
            )
        }
    }
}

@Composable
fun TaskAddButton(modifier: Modifier, function: () -> Unit) {
    Box {
        Button(
            onClick = { function() },
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            shape = RectangleShape
        ) {
            Text(text = "Add a Note")
        }
    }
}


@Composable
fun TaskList(tasks: List<Task>, fillMaxSize: Modifier, viewModel: TaskViewModel) {
    if (tasks.isEmpty()) {
        Box(modifier = fillMaxSize) {
            Text(
                text = "No Items Saved",
                Modifier
                    .wrapContentSize()
                    .align(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    } else {
        LazyColumn(modifier = fillMaxSize) {
            items(tasks) {
                TaskItem(task = it) { id ->
                    viewModel.delete(id)
                }
            }
        }

    }
}

@Composable
fun TaskItem(task: Task, delete: (Long) -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    Card(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(4.dp)
    ) {
        Row {
            Text(
                text = task.description,
                Modifier
                    .padding(16.dp)
                    .weight(0.9F, true)
            )
            Image(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Note",
                Modifier
                    .padding(16.dp)
                    .clickable {
                        showDialog = true
                    },
                colorFilter = ColorFilter.tint(colorResource(id = R.color.red))
            )
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when clicked outside
                showDialog = false
            },
            title = {
                Text(text = "Delete Note")
            },
            text = {
                Text(text = "Are you sure you want to delete this note?")
            },
            confirmButton = {
                TextButton(onClick = {
                    // Perform delete operation here
                    delete(task.id)
                    showDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    // Dismiss the dialog without deleting
                    showDialog = false
                }) {
                    Text("No")
                }
            }
        )
    }
    // Render each task item here
}