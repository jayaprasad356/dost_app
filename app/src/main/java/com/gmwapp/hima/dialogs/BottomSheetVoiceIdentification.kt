package com.gmwapp.hima.dialogs

import android.R.attr.button
import android.app.Dialog
import android.content.Context
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import com.gmwapp.hima.callbacks.OnButtonClickListener
import com.gmwapp.hima.callbacks.OnItemSelectionListener
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.databinding.BottomSheetLogoutBinding
import com.gmwapp.hima.databinding.BottomSheetVoiceIdentificationBinding
import com.gmwapp.hima.retrofit.responses.Country
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.IOException


class BottomSheetVoiceIdentification : BottomSheetDialogFragment() {
    private var onButtonClickListener: OnButtonClickListener? = null
    private var mRecorder: MediaRecorder? = null
    var mFileName: String? =
        Environment.getExternalStorageDirectory().absolutePath + "/AudioRecording.3gp"
    lateinit var binding: BottomSheetVoiceIdentificationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, bundle: Bundle?
    ): View {
        binding = BottomSheetVoiceIdentificationBinding.inflate(layoutInflater)

        initUI()
        binding.tvSpeechText.text = bundle?.getString(DConstants.TEXT)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onButtonClickListener = activity as OnButtonClickListener
        } catch (e: ClassCastException) {
            Log.e("BottomSheetCountry", "onAttach: ClassCastException: " + e.message)
        }
    }


    override fun getTheme(): Int = com.gmwapp.hima.R.style.BottomSheetDialogTheme
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)

    private fun initUI() {
        binding.ivSpeech.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                startRecording()
            } else if (event.action == MotionEvent.ACTION_UP) {
                onButtonClickListener?.onButtonClick()
                mRecorder?.release()
            }
            true
        })
    }

    private fun startRecording() {
        mFileName = Environment.getExternalStorageDirectory().absolutePath
        mFileName += "/AudioRecording.3gp"

        mRecorder = MediaRecorder()

        mRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mRecorder?.setOutputFile(mFileName)
        try {
            mRecorder?.prepare()
        } catch (e: IOException) {
            Log.e("TAG", "prepare() failed")
        }
        mRecorder?.start()
    }
}