package com.example.suprisequizapplication.model

import com.google.gson.annotations.SerializedName

data class SurpriseQuiz (
    val quizTitle: String?,
    val questions: List<Question>?,
)