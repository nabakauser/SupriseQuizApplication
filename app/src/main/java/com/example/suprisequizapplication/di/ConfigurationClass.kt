package com.example.suprisequizapplication.di

import com.example.suprisequizapplication.viewmodel.SurpriseQuizViewModel
import com.example.suprisequizapplication.repository.SurpriseQuizRepository
import org.koin.dsl.module

object ConfigurationClass {
    fun modules() = repositoryModule + viewModelModule
}

val repositoryModule = module {
    single { SurpriseQuizRepository() }
}

val viewModelModule = module {
    single { SurpriseQuizViewModel() }
}
