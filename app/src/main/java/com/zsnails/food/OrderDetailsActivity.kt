package com.zsnails.food

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zsnails.food.databinding.ActivityOrderDetailsBinding
import com.zsnails.food.model.Order
import com.zsnails.food.model.Recipe
import com.zsnails.food.ui.home.RecipesAdapter
import com.zsnails.food.ui.recipe.RecipeActivity
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import java.io.Serializable
import kotlinx.serialization.Serializable as S

class OrderDetailsActivity : AppCompatActivity(), ClickListener<Recipe> {

    private lateinit var launcher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityOrderDetailsBinding
    private val cart: CartViewModel by viewModels()
    private lateinit var order: Order
    private val client = createSupabaseClient(BuildConfig.SUPABASE_URL, BuildConfig.SUPABASE_KEY) {
        install(Postgrest)
        install(Auth)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ::handleResult
        )

        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        order = intent.getSerializableExtra("order") as Order
        binding.orderIdTextBox.text = order.id.toString()
        binding.orderRecipes.layoutManager = LinearLayoutManager(this)
        val adapter = RecipesAdapter(this, this)
        binding.orderRecipes.adapter = adapter
        lifecycleScope.launch {
            val result = client.from("orders_recipes").select(Columns.raw("recetas(*)")) {
                filter {
                    eq("order_id", order.id!!)
                }
            }.decodeList<Response>().map { it.recipes }
            Log.d("ORDERS:RESULT", result.toString())
            adapter.updateDataSet(result)
        }
    }

    @S
    data class Response(
        @SerialName("recetas")
        val recipes: Recipe
    ) : Serializable

    override fun onItemClicked(item: Recipe, position: Int) {
        val intent = Intent(this, RecipeActivity::class.java)
        intent.putExtra("recipe", item)
        intent.putExtra("added?", cart.isAdded(item))
        launcher.launch(intent)
    }

    fun handleResult(result: ActivityResult) {
        when (result.resultCode) {
            CartOperation.ADD.ordinal -> {
                val added = result.data!!.getSerializableExtra("added") as Recipe
                try {
                    cart.addToCart(added)
                } catch (e: ItemInCartException) {
                    Log.e("CART:ERR", e.message.toString())
                }
            }
        }
    }
}