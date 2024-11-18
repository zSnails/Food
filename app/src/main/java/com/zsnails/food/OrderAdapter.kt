package com.zsnails.food

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zsnails.food.model.Order
import java.util.Locale

class OrderAdapter(private val context: Context, private val listener: ClickListener<Order>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    private val orders = ArrayList<Order>() //  = listOf()

    inner class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderId: TextView = view.findViewById(R.id.orderId)
        val orderPrice: TextView = view.findViewById(R.id.orderPrice)
        val createdAt: TextView = view.findViewById(R.id.orderCreationDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.order_item_component, parent, false)
        return OrderViewHolder(v)
    }

    override fun getItemCount() = orders.size

    fun updateDataSet(new: List<Order>) {
        orders.clear()
        orders.addAll(new)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val current = orders[position]
        holder.orderId.text = current.id.toString()
        holder.orderPrice.text = String.format(Locale.US, "¢%.2f", current.price)
        holder.createdAt.text = current.createdAt.toString()
        holder.itemView.setOnClickListener {
            listener.onItemClicked(current, position)
        }
    }
}