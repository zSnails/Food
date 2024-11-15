package com.zsnails.food.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zsnails.food.R
import com.zsnails.food.model.Recipe
import java.util.Locale

class CartAdapter(var context: Context, var items: List<Recipe>): RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.cartItemName)
        val itemPrice: TextView = view.findViewById(R.id.cartItemPrice)
        val createdAt: TextView = view.findViewById(R.id.cartItemDateAdded)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val component = LayoutInflater.from(context).inflate(R.layout.cart_item_component, parent, false)
        return CartViewHolder(component)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val current = items[position]
        holder.itemName.text = current.name
        holder.itemPrice.text = String.format(Locale.US, "¢%.2f", current.price)
        holder.createdAt.text = current.createdAt
    }
}