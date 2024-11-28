package com.gmwapp.dostt.callbacks

interface OnItemSelectionListener<T> {
    abstract val number: T?

    fun onItemSelected(selectedItemDetails: T)
}