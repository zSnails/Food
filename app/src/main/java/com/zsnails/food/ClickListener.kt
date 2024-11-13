package com.zsnails.food

interface ClickListener<T> {
    fun onItemClicked(item: T, position: Int)
}