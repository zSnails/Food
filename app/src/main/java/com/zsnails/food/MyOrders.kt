package com.zsnails.food

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zsnails.food.databinding.ActivityMyOrdersBinding
import com.zsnails.food.databinding.ActivityRecipeBinding
import com.zsnails.food.model.Order
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch

class MyOrders : AppCompatActivity(), ClickListener<Order> {
    private lateinit var binding: ActivityMyOrdersBinding
    private var client = createSupabaseClient(BuildConfig.SUPABASE_URL, BuildConfig.SUPABASE_KEY) {
        install(Postgrest)
        install(Auth)
    }
    private lateinit var user: UserInfo
    private lateinit var state: State
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        state = State.getInstance()
        user = state.user
        binding = ActivityMyOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.orders.layoutManager = LinearLayoutManager(this)
        val adapter = OrderAdapter(baseContext, this)
        binding.orders.adapter = adapter
        lifecycleScope.launch {
            val result = client.from("orders").select {
                filter {
                    eq("owner_id", user.id)
                }
            }
            Log.d("ORDERS:USER:ID", user.id)
            Log.d("ORDERS:SIZE", result.countOrNull().toString())
            adapter.updateDataSet(result.decodeList())
        }

    }

    override fun onItemClicked(item: Order, position: Int) {
        val intent = Intent(this, OrderDetailsActivity::class.java)
        intent.putExtra("order", item)
        startActivity(intent)
    }
}