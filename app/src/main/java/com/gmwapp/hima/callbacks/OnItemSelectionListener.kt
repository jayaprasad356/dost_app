package com.gmwapp.hima.callbacks

interface OnItemSelectionListener<T> {
    abstract val number: T?

    fun onItemSelected(selectedItemDetails: T)
}