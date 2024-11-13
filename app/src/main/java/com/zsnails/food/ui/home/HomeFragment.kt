package com.zsnails.food.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zsnails.food.BuildConfig
import com.zsnails.food.ClickListener
import com.zsnails.food.databinding.FragmentHomeBinding
import com.zsnails.food.model.Recipe
import com.zsnails.food.ui.recipe.RecipeActivity
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class HomeFragment : Fragment(), ClickListener<Recipe> {

    private var _binding: FragmentHomeBinding? = null
    private var client = createSupabaseClient(BuildConfig.SUPABASE_URL, BuildConfig.SUPABASE_KEY) {
        install(Postgrest)
        install(Auth)
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // val homeViewModel =
        //     ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.recipeList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = RecipesAdapter(requireContext(), this)
        binding.recipeList.adapter = adapter

        lifecycleScope.launch {
            val recipes = client.from("recetas").select().decodeList<Recipe>()
            adapter.updateDataSet(recipes)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(item: Recipe, position: Int) {
        val intent = Intent(requireContext(), RecipeActivity::class.java)
        intent.putExtra("recipe", item)
        startActivity(intent)
    }
}