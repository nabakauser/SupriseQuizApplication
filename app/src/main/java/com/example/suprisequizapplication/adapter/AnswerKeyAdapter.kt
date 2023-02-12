package com.example.suprisequizapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.suprisequizapplication.R
import com.example.suprisequizapplication.model.Options

class AnswerKeyAdapter(
    private val optionList: MutableList<Options>,
    private val onAnswerKeySelected: (String) -> Unit

) : RecyclerView.Adapter<AnswerKeyAdapter.AnswerKeyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnswerKeyViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.answerkey_option_layout,
                    parent,
                    false)
        return AnswerKeyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnswerKeyViewHolder, position: Int) {
        val option = optionList[position]
        holder.uiRbOptions.text = option.optionText
        holder.uiRbOptions.isChecked = option.setAnswer == true
    }

    override fun getItemCount(): Int {
        return optionList.size
    }

    inner class AnswerKeyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val uiRbOptions: RadioButton = itemView.findViewById(R.id.uiRbAnswerKeyOption)

        init {
            uiRbOptions.setOnClickListener {
                onAnswerKeySelected(optionList[adapterPosition].id ?: "")
                notifyDataSetChanged()
            }
        }
    }
}