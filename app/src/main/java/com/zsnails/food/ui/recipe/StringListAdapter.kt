package com.zsnails.food.ui.recipe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zsnails.food.R

class StringListAdapter(private val context: Context) :
    RecyclerView.Adapter<StringListAdapter.InstructionViewHolder>() {

    private var instructions = ArrayList<String>()

    inner class InstructionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val instruction: TextView? = itemView.findViewById(R.id.instructionText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionViewHolder {
        val holder =
            LayoutInflater.from(context).inflate(R.layout.ingredient_list_component, parent, false)

        return InstructionViewHolder(holder)
    }

    override fun getItemCount(): Int {
        return instructions.size
    }

    override fun onBindViewHolder(holder: InstructionViewHolder, position: Int) {
        holder.instruction?.text = instructions[position]
    }

    fun setData(data: List<String>) {
        instructions.clear()
        instructions.addAll(data)
        notifyDataSetChanged()
    }
}