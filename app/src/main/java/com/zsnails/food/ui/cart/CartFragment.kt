package com.zsnails.food.ui.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.zsnails.food.BuildConfig
import com.zsnails.food.CartViewModel
import com.zsnails.food.R
import com.zsnails.food.State
import com.zsnails.food.databinding.FragmentDashboardBinding
import com.zsnails.food.model.Order
import com.zsnails.food.model.OrdersRecipes
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import java.util.Locale

class CartFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val cart: CartViewModel by activityViewModels()
    private var client = createSupabaseClient(BuildConfig.SUPABASE_URL, BuildConfig.SUPABASE_KEY) {
        install(Postgrest)
        install(Auth)
    }
    private var state: State = State.getInstance()
    private lateinit var adapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // user = client.auth.currentUserOrNull()!!
        // Log.d("CART:USER", user.id)

        updatePrice()
        binding.cart.layoutManager = LinearLayoutManager(requireContext())
        adapter = CartAdapter(requireContext(), cart.cart.value!!, cart, ::onRemoved)
        binding.cart.adapter = adapter
        cart.cart.observe(viewLifecycleOwner) {
            updatePrice()
        }
        binding.toCheckout.setOnClickListener(::onCheckout)
        binding.toCheckout.isEnabled = cart.getSize() > 0
        return root
    }

    private fun updatePrice() {
        binding.cartTotalPrice.text = cartPriceString()
        binding.toCheckout.isEnabled = cart.getSize() > 0
    }

    fun onCheckout(view: View) {
        Toast.makeText(requireContext(), R.string.unimplemented_generic_label, Toast.LENGTH_SHORT)
            .show()
        lifecycleScope.launch {
            val order = Order(
                ownerId = state.user.id,
                price = cartPrice(),
            )

            val created = client.from("orders").insert(order) {
                select()
                single()
            }.decodeAs<Order>()

            val orders = cart.cart.value!!.map {
                OrdersRecipes(
                    recipeId = it.id,
                    orderId = created.id
                )
            }

            client.from("orders_recipes").insert(orders)
            adapter.clean()
            updatePrice()
        }
    }

    private fun onRemoved() = updatePrice()

    private fun cartPrice() = cart.cart.value!!.sumOf { it.price }

    private fun cartPriceString() = String.format(Locale.US, "¢%.2f", cartPrice())

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}