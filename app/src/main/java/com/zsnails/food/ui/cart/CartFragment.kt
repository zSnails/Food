package com.zsnails.food.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.zsnails.food.CartViewModel
import com.zsnails.food.databinding.FragmentDashboardBinding
import java.util.Locale

class CartFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val cart: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.cartTotalPrice.text = cartPrice()
        binding.cart.layoutManager = LinearLayoutManager(requireContext())
        binding.cart.adapter = CartAdapter(requireContext(), cart.cart.value!!)
        cart.cart.observe(viewLifecycleOwner) {
            binding.cartTotalPrice.text = cartPrice()
        }

        return root
    }

    private fun cartPrice(): String {
        return String.format(Locale.US, "¢%.2f", cart.cart.value!!.sumOf { it.price })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}