package com.zsnails.food.ui.recipe

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zsnails.food.BuildConfig
import com.zsnails.food.CartOperation
import com.zsnails.food.R
import com.zsnails.food.databinding.ActivityRecipeBinding
import com.zsnails.food.model.Ingredient
import com.zsnails.food.model.Recipe
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import java.io.Serializable
import java.util.Locale

class RecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeBinding
    private lateinit var client: SupabaseClient

    @kotlinx.serialization.Serializable
    data class Response(
        @SerialName("ingredientes")
        var ingredient: Ingredient
    ) : Serializable

    private lateinit var recipe: Recipe

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: this does not work without a server of our own,
        //  so we need to either add in app payment or find something else
        // val config = CoreConfig(BuildConfig.PAYPAL_CLIENT_ID, environment = Environment.SANDBOX)
        // val cardClient = CardClient(this@RecipeActivity, config)
        // var cardRequest = CardRequest()

        binding = ActivityRecipeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        client = createSupabaseClient(BuildConfig.SUPABASE_URL, BuildConfig.SUPABASE_KEY) {
            install(Postgrest)
        }

        binding.instructionBox.layoutManager = LinearLayoutManager(this)
        val adapter = StringListAdapter(this)
        binding.instructionBox.adapter = adapter

        recipe = intent.getSerializableExtra("recipe", Recipe::class.java)!!
        binding.viewRecipeTitle.text = recipe.name
        binding.viewRecipeDescription.text = recipe.description
        recipe.instructions?.let {
            adapter.setData(it)
        }
        binding.viewPrice.text = String.format(Locale.US, "¢%.2f", recipe.price)

        val isAdded = intent.getBooleanExtra("added?", false)

        binding.addToCartButton.isEnabled = !isAdded

        binding.ingredientList.layoutManager = LinearLayoutManager(this)
        val adapter2 = StringListAdapter(this)
        binding.ingredientList.adapter = adapter2
        lifecycleScope.launch {
            val got = client.from("receta_ingrediente").select(Columns.raw("ingredientes(*)")) {
                filter {
                    eq("receta_id", recipe.id)
                }
            }.decodeList<Response>()

            got.map {
                it.ingredient.name
            }.let {
                adapter2.setData(it)
            }
        }
    }

    fun onAddToCart(view: View) {
        val result = Intent()
        result.putExtra("added", recipe)
        setResult(CartOperation.ADD.ordinal, result)
        binding.addToCartButton.isEnabled = false
    }
}