package com.zsnails.food.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zsnails.food.ClickListener
import com.zsnails.food.R
import com.zsnails.food.model.Recipe
import com.zsnails.food.ui.home.RecipesAdapter.RecipeViewHolder

class RecipesAdapter(private val context: Context, private val listener: ClickListener<Recipe>) :
    RecyclerView.Adapter<RecipeViewHolder>() {

    private val allRecipes = ArrayList<Recipe>()

    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.recipeTitle)
        val description = itemView.findViewById<TextView>(R.id.recipeDescription)
        val createdAt = itemView.findViewById<TextView>(R.id.recipeCreationDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val layout =
            LayoutInflater.from(context).inflate(R.layout.recipe_list_component, parent, false)
        val viewHolder = RecipeViewHolder(layout)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return allRecipes.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.title.text = allRecipes[position].name
        holder.description.text = allRecipes[position].description
        holder.createdAt.text = allRecipes[position].createdAt.toString()
        holder.itemView.setOnClickListener {
            listener.onItemClicked(allRecipes[position], position)
        }
    }

    fun updateDataSet(data: List<Recipe>) {
        allRecipes.clear()
        allRecipes.addAll(data)
        notifyDataSetChanged()
    }
}