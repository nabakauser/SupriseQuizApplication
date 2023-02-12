package com.example.suprisequizapplication.model


data class Question (
    var id : String? = null,
    var text: String?,
    var options: MutableList<Options>?,
    var image: String?,
)