package com.example.core3marcogiacoppo

import java.time.LocalDateTime

data class Meeting (
    val id: Int,
    val group: String,
    val location: String,
    val type: String,
    val datetime: String,
    val dateTimeObject: LocalDateTime
        )