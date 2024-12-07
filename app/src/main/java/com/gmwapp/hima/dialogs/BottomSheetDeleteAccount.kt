package com.gmwapp.hima.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmwapp.hima.R
import com.gmwapp.hima.callbacks.OnButtonClickListener
import com.gmwapp.hima.callbacks.OnItemSelectionListener
import com.gmwapp.hima.databinding.BottomSheetDeleteAccountBinding
import com.gmwapp.hima.databinding.BottomSheetLogoutBinding
import com.gmwapp.hima.retrofit.responses.Country
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BottomSheetDeleteAccount : BottomSheetDialogFragment() {
    lateinit var binding: BottomSheetDeleteAccountBinding
    private var onButtonClickListener: OnButtonClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetDeleteAccountBinding.inflate(layoutInflater)

        initUI()
        return binding.root
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onButtonClickListener = activity as OnButtonClickListener?
        } catch (e: ClassCastException) {
            Log.e("BottomSheetCountry", "onAttach: ClassCastException: " + e.message)
        }
    }

    private fun initUI() {
        binding.btnDeleteAccount.setOnClickListener({
            onButtonClickListener?.onButtonClick()

        })
    }
}