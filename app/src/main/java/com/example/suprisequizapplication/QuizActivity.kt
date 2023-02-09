package com.example.suprisequizapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.suprisequizapplication.adapter.AnswerKeyAdapter
import com.example.suprisequizapplication.adapter.QuestionAdapter
import com.example.suprisequizapplication.databinding.ActivitySurpriseQuizBinding
import com.example.suprisequizapplication.model.Question
import com.example.suprisequizapplication.viewmodel.SurpriseQuizViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel

class QuizActivity : AppCompatActivity() {
    private var binding: ActivitySurpriseQuizBinding? = null
    private val surpriseQuizViewModel: SurpriseQuizViewModel by viewModel()
    private var quizAdapter: QuestionAdapter? = null
    private var answerKeyAdapter: AnswerKeyAdapter? = null

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

    private fun setUpAdapter(questionList: List<Question>) {
        quizAdapter = QuestionAdapter(
            questionList = questionList.toMutableList(),
            onAddQuestion = {
                surpriseQuizViewModel.createSurpriseQuizQuestion()
            },
            onCopyQuestion = { position, question ->
                surpriseQuizViewModel.copyQuizCard(position, question)
            },
            onDeleteQuestion = { position ->
                surpriseQuizViewModel.deleteQuizCard(position)
            },
            onOptionTextEntered = { questionPosition, optionPosition, optionText ->
                surpriseQuizViewModel.onOptionTextEntered(
                    questionPosition,
                    optionPosition,
                    optionText
                )
            },
            onOptionAddButtonClicked = { questionPosition ->
                surpriseQuizViewModel.onOptionAdded(questionPosition)
            },
            onOptionDeleted = { questionPosition, optionPosition ->
                surpriseQuizViewModel.onOptionDeleted(questionPosition, optionPosition)
            },
            onQuestionTextEntered = { questionPosition, questionText ->
                surpriseQuizViewModel.onQuestionTextEntered(questionPosition, questionText)
            },
            onAnswerKeySelected = { questionPosition, radioBtnPosition ->
              //  surpriseQuizViewModel.onRadioButtonSelected(questionPosition, radioBtnPosition)
            },
            onSetAnswerKeyClicked = { position ->
                showAnswerKeyBottomSheet(position)
            },

        )
        binding?.uiRvQuiz?.adapter = quizAdapter
        (binding?.uiRvQuiz?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

    }

    private fun setUpListeners() {
        binding?.uiBtnAddQuestions?.setOnClickListener {
            surpriseQuizViewModel.createSurpriseQuizQuestion()
        }
    }

    private fun showAnswerKeyBottomSheet(position: Int) {
        val answerKeyBottomSheet = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.set_answer_key_layout, null)
        val uiIvCloseButton = view.findViewById<ImageView>(R.id.uiIvCloseBottomTab)
        var uiTvQuestion = view.findViewById<TextView>(R.id.uiTvQuestion)

        val question = surpriseQuizViewModel.questions.value?.get(position)
        uiTvQuestion.text = question?.text
        answerKeyAdapter = AnswerKeyAdapter(
            optionsList = question?.options?.toMutableList() ?: mutableListOf(),
            onAnswerKeySelected = { optionPosition ->
                surpriseQuizViewModel.onAnswerKeySelected(position,optionPosition)
                answerKeyBottomSheet.dismiss()
            }
        )

        uiIvCloseButton.setOnClickListener {
            answerKeyBottomSheet.dismiss()
        }
        answerKeyBottomSheet.setCancelable(false)
        answerKeyBottomSheet.setContentView(view)
        val optionsRv = view.findViewById<RecyclerView>(R.id.uiRvAnswerKey)
        optionsRv.adapter = answerKeyAdapter
        answerKeyBottomSheet.show()
    }
}
