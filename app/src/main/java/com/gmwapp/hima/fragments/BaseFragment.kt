package com.gmwapp.hima.fragments

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gmwapp.hima.R
import com.gmwapp.hima.constants.DConstants

open class BaseFragment : Fragment() {
    fun showErrorMessage(message: String) {
        if (message == DConstants.NO_NETWORK) {
            Toast.makeText(
                context, getString(R.string.please_try_again_later), Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                context, message, Toast.LENGTH_LONG
            ).show()
        }
    }
}