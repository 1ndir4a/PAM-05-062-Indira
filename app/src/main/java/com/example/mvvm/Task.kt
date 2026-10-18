package com.example.mvvm

data class Task(
    val id: Int,
    val title: String,
    val done: Boolean = false
)