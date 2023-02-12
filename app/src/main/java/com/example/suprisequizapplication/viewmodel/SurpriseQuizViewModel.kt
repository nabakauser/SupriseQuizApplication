package com.example.suprisequizapplication.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.suprisequizapplication.model.Options
import com.example.suprisequizapplication.model.Question

class SurpriseQuizViewModel : ViewModel() {

    private var questionList: ArrayList<Question> = arrayListOf()

    private val questionsLD = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> = questionsLD

    init {
        createSurpriseQuizQuestion()
    }

    fun createSurpriseQuizQuestion() {
        val questionSize = questionList.size
        Log.d("questionSize", "createSurpriseQuizQuestion: $questionSize")

            val question = Question(
                id = System.currentTimeMillis().toString(),
                text = "",
                image = "",
                options = arrayListOf(
                    Options(
                        id = System.currentTimeMillis().toString(),
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
            val optionArrayList = question.options?.mapIndexed { index, option ->
                Options(
                    id = "$index" + System.currentTimeMillis().toString(),
                    optionText = option.optionText,
                    setAnswer = false,
                )
            }
            val copiedCard = Question(
                id = System.currentTimeMillis().toString(),
                text = question.text,
                image = question.image,
                options = optionArrayList?.toMutableList(),
            )
            questionList.add(position + 1, copiedCard)
            questionsLD.value = questionList
    }

    fun onQuestionTextEntered(questionPosition: Int, questionText: String) {
        val question = questionList[questionPosition]
        question.text = questionText
        Log.d("questionText", "onQuestionTextEntered: $questionText")
    }

    fun onOptionAdded(questionPosition: Int) {
        val question = questionList[questionPosition]
        val option = Options(
            id = System.currentTimeMillis().toString(),
            optionText = "",
            setAnswer = false
        )
        val optionSize = question.options?.size
        if (optionSize != null) {
            if(optionSize < 4) {
                question.options?.add(option)
            }
        }
        //questionsLD.value = questionList
    }

    fun onOptionTextEntered(questionPosition: Int, optionPosition: Int, optionText: String) {
        Log.d("optionText", "onOptionTextEntered: $optionText")
        val question = questionList[questionPosition]
        val option = question.options?.get(optionPosition)
        option?.optionText = optionText

    }

    fun onOptionDeleted(questionPosition: Int, optionPosition: Int) {
        Log.d("questionPosition", "onOptionDeleted: $questionPosition : $optionPosition")
        val question = questionList[questionPosition].options?.removeAt(optionPosition)
        Log.d("questionPosition", "afterRemoval: $question")
        Log.d("questionPosition", "questionList: $questionList")
        questionsLD.value = questionList
    }

    fun onAnswerKeySelected(questionPosition: Int, optionId: String) {
        val question = questionList[questionPosition]
        question.options?.forEach { options ->
            options.setAnswer = false
            if (options.id == optionId)
                options.setAnswer = true
        }
        questionsLD.value = questionList
        Log.d("questionList", "onAnswerKeySelected: $questionList")
    }

    fun setImage(position: Int, image: Uri) {
        val question = questionList[position]
        question.image = image.toString()
        questionsLD.value = questionList
    }
}
