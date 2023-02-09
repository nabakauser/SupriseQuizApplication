package com.example.suprisequizapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.suprisequizapplication.adapter.QuestionAdapter
import com.example.suprisequizapplication.databinding.ActivitySurpriseQuizBinding
import com.example.suprisequizapplication.model.Question
import com.example.suprisequizapplication.viewmodel.SurpriseQuizViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class QuizActivity : AppCompatActivity() {
    private var binding: ActivitySurpriseQuizBinding? = null
    private val surpriseQuizViewModel: SurpriseQuizViewModel by viewModel()
    private var quizAdapter: QuestionAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurpriseQuizBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setUpObserver()
        setUpListeners()
    }

    private fun setUpObserver() {
        surpriseQuizViewModel.questions.observe(this) { questionList ->
            setUpAdapter(questionList)
        }
    }

    private fun setUpAdapter(questionList : List<Question>) {
        quizAdapter = QuestionAdapter(
            questionList = questionList.toMutableList(),
            onAddQuestion = {
                surpriseQuizViewModel.createSurpriseQuizQuestion()
            },
            onCopyQuestion = { position , question->
                surpriseQuizViewModel.copyQuizCard(position,question)
            },
            onDeleteQuestion = { position ->
                surpriseQuizViewModel.deleteQuizCard(position)
            },
            onOptionTextEntered = { questionPosition, optionPosition, optionText ->
                surpriseQuizViewModel.onOptionTextEntered(questionPosition,optionPosition,optionText)
            },
            onOptionAddButtonClicked = { questionPosition ->
                surpriseQuizViewModel.onOptionAdded(questionPosition)
            },
            onOptionDeleted = { questionPosition, optionPosition ->
                surpriseQuizViewModel.onOptionDeleted(questionPosition, optionPosition)
            },
            onQuestionTextEntered = { questionPosition, questionText ->
                surpriseQuizViewModel.onQuestionTextEntered(questionPosition,questionText)
            },
            onAnswerKeySelected = { questionPosition, radioBtnPosition ->
                surpriseQuizViewModel.onAnswerKeySelected(questionPosition,radioBtnPosition)
            }
        )
        binding?.uiRvQuiz?.adapter = quizAdapter
    }

    private fun setUpListeners() {
        binding?.uiBtnAddQuestions?.setOnClickListener {
            surpriseQuizViewModel.createSurpriseQuizQuestion()
        }
    }
}