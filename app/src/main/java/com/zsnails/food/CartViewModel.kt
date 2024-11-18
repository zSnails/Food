package com.zsnails.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zsnails.food.model.Recipe

class CartViewModel : ViewModel() {
    private val _cart: MutableLiveData<ArrayList<Recipe>> = MutableLiveData<ArrayList<Recipe>>().apply {
        value = ArrayList();
    }

    fun clean() {
        _cart.value!!.clear()
    }

    fun getSize(): Int {
        return cart.value!!.size
    }

    val cart: LiveData<ArrayList<Recipe>> = _cart

    fun addToCart(recipe: Recipe) {
        if (isAdded(recipe)) throw ItemInCartException("the recipe ${recipe.name } has already been added to the cart")
        _cart.value!!.add(recipe)
    }

    fun isAdded(recipe: Recipe): Boolean {
        return _cart.value!!.contains(recipe)
    }

    fun removeFromCart(recipe: Recipe): Int {
        val idx = _cart.value!!.indexOf(recipe)
        _cart.value!!.removeAt(idx)
        return idx
    }
}