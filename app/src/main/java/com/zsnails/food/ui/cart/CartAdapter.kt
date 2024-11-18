package com.zsnails.food.ui.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zsnails.food.CartViewModel
import com.zsnails.food.R
import com.zsnails.food.model.Recipe
import java.util.Locale

class CartAdapter(
    private val context: Context,
    private val items: MutableList<Recipe>,
    private val vm: CartViewModel,
    private val onRemove: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.cartItemName)
        val itemPrice: TextView = view.findViewById(R.id.cartItemPrice)
        val createdAt: TextView = view.findViewById(R.id.cartItemDateAdded)
        val removeFromCart: Button = view.findViewById(R.id.removeFromCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val component =
            LayoutInflater.from(context).inflate(R.layout.cart_item_component, parent, false)
        return CartViewHolder(component)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return items[position].id
    }

    fun clean() {
        vm.clean()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val current = items[position]
        holder.itemName.text = current.name
        holder.itemPrice.text = String.format(Locale.US, "¢%.2f", current.price)
        holder.createdAt.text = current.createdAt
        holder.removeFromCart.setOnClickListener {
            it.isEnabled = false
            val idx = vm.removeFromCart(current)
            onRemove()
            notifyItemRemoved(idx)
        }
    }
}