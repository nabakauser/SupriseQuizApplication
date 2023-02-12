package com.example.suprisequizapplication

import android.annotation.SuppressLint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.suprisequizapplication.adapter.AnswerKeyAdapter
import com.example.suprisequizapplication.adapter.QuestionAdapter
import com.example.suprisequizapplication.databinding.ActivitySurpriseQuizBinding
import com.example.suprisequizapplication.model.Question
import com.example.suprisequizapplication.viewmodel.SurpriseQuizViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class QuizActivity : AppCompatActivity() {
    private var binding: ActivitySurpriseQuizBinding? = null
    private val surpriseQuizViewModel: SurpriseQuizViewModel by viewModel()
    private var quizAdapter: QuestionAdapter? = null
    private var answerKeyAdapter: AnswerKeyAdapter? = null
    private var setImageUri: Uri? = null
    private var questionPosition: Int? = null
    private val galleryResultContracts by lazy {
        registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            questionPosition?.let { questionPosition ->
                if (imageUri != null) {
                    surpriseQuizViewModel.setImage(questionPosition, image= imageUri)
                }
            }
        }
    }
    private val cameraResultContracts by lazy {
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
                questionPosition?.let { questionPosition ->
                    if (isSuccess) {
                        setImageUri?.let { imageUri ->
                            surpriseQuizViewModel.setImage(questionPosition,imageUri)
                        }
                    }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySurpriseQuizBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        galleryResultContracts
        cameraResultContracts
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
                if (questionList.size < 4) {
                    surpriseQuizViewModel.createSurpriseQuizQuestion()
                } else {
                    Toast.makeText(this, "Only 4 questions are allowed", Toast.LENGTH_SHORT).show()
                }
            },
            onCopyQuestion = { position, question ->
                if (questionList.size < 4) {
                    surpriseQuizViewModel.copyQuizCard(position, question)
                } else {
                    Toast.makeText(this, "Only 4 questions are allowed", Toast.LENGTH_SHORT).show()
                }
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
                val optionSize = questionList[questionPosition].options?.size
                if (optionSize != null) {
                    if (optionSize < 4) {
                        surpriseQuizViewModel.onOptionAdded(questionPosition)
                    } else {
                        Toast.makeText(this, "Only 4 options are allowed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
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
                val question = questionList[position]
                val optionList = question.options
                val optionSize = optionList?.size

                if(question.text?.isEmpty() == false) {
                    if (optionSize != null) {
                        if(optionSize > 1){
                            var flag = false
                            optionList.forEach {option ->
                                if(option.optionText?.isEmpty() == true) {
                                    flag = true
                                }
                            }
                            if(flag) {
                                Toast.makeText(this, "Please set all options", Toast.LENGTH_SHORT).show()
                            }
                            else {
                                setAnswerKeyBottomSheet(position)
                            }
                        }else{
                            Toast.makeText(this,"A minimum of 2 options are required",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else {
                    Toast.makeText(this, "Please set both question and options", Toast.LENGTH_SHORT).show()
                }
            },
            onImageQuestionClicked = { position ->
                setUpAddImageBottomSheet(position)
            }
        )
        binding?.uiRvQuiz?.adapter = quizAdapter
        (binding?.uiRvQuiz?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

    }

    private fun setUpListeners() {
        val questionList = surpriseQuizViewModel.questions.value
        Log.d("questionSize", "setUpListeners: $questionList")
        binding?.uiBtnAddQuestions?.setOnClickListener {
            if (questionList != null) {
                if (questionList.size < 4) {
                    surpriseQuizViewModel.createSurpriseQuizQuestion()
                } else {
                    Toast.makeText(this, "Only 4 questions are allowed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n", "InflateParams")
    private fun setAnswerKeyBottomSheet(position: Int) {
        val answerKeyBottomSheet = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.set_answer_key_layout, null)
        val uiIvCloseButton = view.findViewById<ImageView>(R.id.uiIvCloseBottomTab)
        val uiTvQuestion = view.findViewById<TextView>(R.id.uiTvQuestion)

        val question = surpriseQuizViewModel.questions.value?.get(position)
        uiTvQuestion.text = "${position+1}. " + question?.text
        
        answerKeyAdapter = AnswerKeyAdapter(
            optionList = question?.options?.toMutableList() ?: mutableListOf(),
            onAnswerKeySelected = { optionId ->
                surpriseQuizViewModel.onAnswerKeySelected(position,optionId)
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

    @SuppressLint("InflateParams")
    private fun setUpAddImageBottomSheet(position: Int) {

        val addImageBottomSheet = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.get_image_layout, null)
        val uiIvCloseButton = view.findViewById<ImageView>(R.id.uiIvCloseBottomSheet)
        val uiTvGetImageFromGallery = view.findViewById<TextView>(R.id.uiTvGetImageFromGallery)
        val uiTvGetImageFromCamera = view.findViewById<TextView>(R.id.uiTvGetImageFromCamera)

        uiTvGetImageFromGallery.setOnClickListener {
            questionPosition = position
            galleryResultContracts.launch("image/*")
            addImageBottomSheet.dismiss()
        }

        uiTvGetImageFromCamera.setOnClickListener {
            getTmpFileUri().let { uri ->
                setImageUri = uri
                cameraResultContracts.launch(setImageUri)
                addImageBottomSheet.dismiss()
            }
        }

        uiIvCloseButton.setOnClickListener {
            addImageBottomSheet.dismiss()
        }
        addImageBottomSheet.setCancelable(true)
        addImageBottomSheet.setContentView(view)
        addImageBottomSheet.show()
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(applicationContext, "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }

}
