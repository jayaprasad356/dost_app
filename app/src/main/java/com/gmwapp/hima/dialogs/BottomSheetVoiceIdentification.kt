package com.gmwapp.hima.dialogs

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
import com.gmwapp.hima.databinding.BottomSheetVoiceIdentificationBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import java.io.IOException


class BottomSheetVoiceIdentification : BottomSheetDialogFragment() {
    private var audiofile: File? = null
    private var onItemSelectionListener: OnItemSelectionListener<String?>? = null
    private var mRecorder: MediaRecorder? = null
    lateinit var binding: BottomSheetVoiceIdentificationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetVoiceIdentificationBinding.inflate(layoutInflater)

        initUI()
        binding.tvSpeechText.text = arguments?.getString(DConstants.TEXT)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onItemSelectionListener = activity as OnItemSelectionListener<String?>
        } catch (e: ClassCastException) {
            Log.e("BottomSheetCountry", "onAttach: ClassCastException: " + e.message)
        }
    }


    override fun getTheme(): Int = com.gmwapp.hima.R.style.BottomSheetDialogTheme
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        BottomSheetDialog(requireContext(), theme)

    private fun initUI() {
        binding.clMicrophone.setOnTouchListener(OnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                startRecording()
            } else if (event.action == MotionEvent.ACTION_UP) {
                onItemSelectionListener?.onItemSelected(audiofile.toString())
                mRecorder?.release()
            }
            true
        })
    }

    private fun startRecording() {
        val dir = context?.cacheDir;
        try {
            audiofile = File.createTempFile(DConstants.AUDIO_FILE, ".mp3", dir)
        } catch (e: IOException) {
            Log.e("TAG", "external storage access error"+e.stackTraceToString())

            return
        }

        mRecorder = MediaRecorder()

        mRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mRecorder?.setOutputFile(audiofile?.getAbsolutePath())
        try {
            mRecorder?.prepare()
            mRecorder?.start()
        } catch (e: IOException) {
            Log.e("TAG", "prepare() failed"+e.stackTraceToString())
        }
    }
}