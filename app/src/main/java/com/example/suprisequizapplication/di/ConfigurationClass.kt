package com.example.suprisequizapplication.di

import com.example.suprisequizapplication.viewmodel.SurpriseQuizViewModel
import org.koin.dsl.module

object ConfigurationClass {
    fun modules() = viewModelModule
}

val viewModelModule = module {
    single { SurpriseQuizViewModel() }
}
