package com.gmwapp.hima.utils

import com.gmwapp.hima.viewmodels.GiftViewModel

object GiftViewModelProvider {
    lateinit var giftViewModel: GiftViewModel

    fun init(viewModel: GiftViewModel) {
        giftViewModel = viewModel
    }
}
