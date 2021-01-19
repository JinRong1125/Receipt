package com.shinei.receipt.feature.camera

import android.content.Context
import android.graphics.Bitmap
import com.shinei.receipt.entity.Response
import com.shinei.receipt.task.RecognizeTask

class CameraModel(private val cameraContract: CameraContract) {

    private val recognizeTask by lazy { RecognizeTask() }

    fun recognize(context: Context, bitmap: Bitmap) {
        recognizeTask(context, bitmap).let {
            if (it is Response.Success) {
                cameraContract.onRecognized(it.data)
            }
        }
    }
}