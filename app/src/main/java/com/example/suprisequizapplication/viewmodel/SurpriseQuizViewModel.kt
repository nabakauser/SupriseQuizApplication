package com.example.suprisequizapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.suprisequizapplication.model.Options
import com.example.suprisequizapplication.model.Question

class SurpriseQuizViewModel(
) : ViewModel() {

    private var questionList: ArrayList<Question> = arrayListOf()

    private val questionsLD = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> = questionsLD

    init {
        createSurpriseQuizQuestion()
    }

    fun createSurpriseQuizQuestion() {
        val question = Question(
            text = "",
            options = arrayListOf(
                Options(
                    optionText = "",
                    setAnswer = false
                )
            )
        )
        questionList.add(question)
        questionsLD.value = questionList
    }

    fun deleteQuizCard(position: Int) {
        questionList.removeAt(position)
        questionsLD.value = questionList
    }

    fun copyQuizCard(position: Int, question: Question) {
        val optionArrayList = arrayListOf<Options>()
        question.options?.forEach { option ->
            optionArrayList.add(option)
        }
        val copiedCard = Question(
            text = question.text,
            options = optionArrayList,
        )
        questionList.add(position + 1, copiedCard)
        questionsLD.value = questionList
    }

    fun onOptionTextEntered(questionPosition: Int, optionPosition: Int, optionText: String) {
        Log.d("optionText", "onOptionTextEntered: ${optionText}")
        val question = questionList[questionPosition]
        val option = question.options?.get(optionPosition)
        option?.optionText = optionText

    }

    fun onQuestionTextEntered(questionPosition: Int, questionText: String) {
        val question = questionList[questionPosition]
        question.text = questionText
        Log.d("questionText", "onQuestionTextEntered: ${questionText}")
    }

    fun onOptionAdded(questionPosition: Int) {
        val question = questionList[questionPosition]
        val option = Options(optionText = "", setAnswer = false)
        question.options?.add(option)
        //questionsLD.value = questionList

    }

    fun onOptionDeleted(questionPosition: Int, optionPosition: Int) {
        Log.d("questionPosition", "onOptionDeleted: ${questionPosition} : ${optionPosition}")
        val question = questionList[questionPosition].options?.removeAt(optionPosition)
        Log.d("questionPosition", "afterRemoval: ${question}")
        Log.d("questionPosition", "questionList: ${questionList}")
        questionsLD.value = questionList
    }

    fun onAnswerKeySelected(questionPosition: Int, radioBtnPosition: Int) {
        Log.e("questionPosition","questionPosition:$questionPosition")
        Log.e("radioBtnPosition","radioBtnPosition:$radioBtnPosition")
        val question = questionList[questionPosition]
            question.options?.forEachIndexed { optionIndex, options ->
            options.setAnswer = false
            if (optionIndex == radioBtnPosition)
                options.setAnswer = true
        }
        questionsLD.value = questionList
    }

    fun onRadioButtonSelected(questionPosition: Int, radioBtnPosition: Int) {
        Log.e("questionPosition","questionPosition:$questionPosition")
        Log.e("radioBtnPosition","radioBtnPosition:$radioBtnPosition")
        val question = questionList[questionPosition]
        question.options?.forEach { options ->
            options.setAnswer = false
        }
        questionsLD.value = questionList
    }
}
