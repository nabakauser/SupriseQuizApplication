package com.example.suprisequizapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.suprisequizapplication.R
import com.example.suprisequizapplication.model.Options

class OptionsAdapter(
    private val optionsList: List<Options>,
    private val onOptionDeleted: (Int) -> Unit,
    private val onOptionTextEntered: (Int, String) -> Unit,
    private val onAnswerKeySelected: (Int) -> Unit
) : RecyclerView.Adapter<OptionsAdapter.OptionsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OptionsViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.option_layout,
                    parent,
                    false)
        return OptionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {
        val option = optionsList[position]
        holder.uiEtOption.hint = "Option ${position+1} "
        holder.uiEtOption.setText(option.optionText)
        holder.uiRbOptions.isChecked = option.setAnswer == true

        holder.uiEtOption.doAfterTextChanged { optionText ->
            option.optionText = optionText.toString()
            onOptionTextEntered(position, optionText.toString())
            //notifyDataSetChanged() -> crash
        }
    }

    override fun getItemCount(): Int {
        return optionsList.size
    }

    inner class OptionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val uiRbOptions: RadioButton = itemView.findViewById(R.id.uiRbOption)
        val uiEtOption: AppCompatEditText = itemView.findViewById(R.id.uiEtOptions)
        private val uiIvDeleteOption: ImageView = itemView.findViewById(R.id.uiIvDeleteOption)

        init {
            uiIvDeleteOption.setOnClickListener {
                onOptionDeleted(adapterPosition)
                notifyDataSetChanged()
            }
            uiRbOptions.setOnClickListener {
                onAnswerKeySelected(adapterPosition)
                notifyDataSetChanged()
            }
        }
    }
}