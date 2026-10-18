package com.example.mvvm

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
class TaskViewModelTest {
    private class FakeRepository : TaskRepository() {
        override fun loadInitialTasks(): List<Task> = listOf(
            Task(1, "First"),
            Task(2, "Second", done = true)
        )
    }

    @Test
    fun toggleDone_flipsTheTaskState() {
        val viewModel = TaskViewModel(FakeRepository())
        viewModel.toggleDone(1)
        assertTrue(viewModel.tasks.value.first { it.id == 1 }.done)
    }
    @Test
    fun toggleDone_twice_returnsToOriginalState() {
        val viewModel = TaskViewModel(FakeRepository())
        viewModel.toggleDone(2)
        viewModel.toggleDone(2)
        assertTrue(viewModel.tasks.value.first { it.id == 2 }.done)
    }
    @Test
    fun addTask_appendsWithNextId() {
        val viewModel = TaskViewModel(FakeRepository())
        viewModel.addTask("Third")
        assertEquals(3, viewModel.tasks.value.size)
        assertEquals(3, viewModel.tasks.value.last().id)
        assertEquals("Third", viewModel.tasks.value.last().title)
    }

    @Test
    fun addTask_ignoresBlankTitle() {
        val viewModel = TaskViewModel(FakeRepository())
        viewModel.addTask(" ")
        assertEquals(2, viewModel.tasks.value.size)
    }
    @Test
    fun deleteTask_removesOnlyThatTask() {
        val viewModel = TaskViewModel(FakeRepository())

        viewModel.deleteTask(1)

        val tasks = viewModel.tasks.value
        assertEquals(1, tasks.size)
        assertEquals(2, tasks.first().id)
    }

    @Test
    fun deleteTask_withUnknownId_changesNothing() {
        val viewModel = TaskViewModel(FakeRepository())

        viewModel.deleteTask(999)

        assertEquals(2, viewModel.tasks.value.size)
    }

    @Test
    fun deletingAllTasks_leavesEmptyList() {
        val viewModel = TaskViewModel(FakeRepository())

        viewModel.deleteTask(1)
        viewModel.deleteTask(2)

        assertTrue(viewModel.tasks.value.isEmpty())
    }

    @Test
    fun emptyRepository_startsWithEmptyList() {
        val viewModel = TaskViewModel(EmptyRepository())

        assertTrue(viewModel.tasks.value.isEmpty())
    }
    private class EmptyRepository : TaskRepository() {
        override fun loadInitialTasks(): List<Task> = emptyList()
    }

}