package com.shinei.receipt.task

import android.content.Context
import android.graphics.Bitmap
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import com.shinei.receipt.entity.Response

class RecognizeTask {

    operator fun invoke(context: Context, bitmap: Bitmap): Response<String> {
        val stringBuilder = StringBuilder()
        val textBlocks = TextRecognizer.Builder(context).build()
            .detect(Frame.Builder().setBitmap(bitmap).build())
        for (i in 0 until textBlocks.size()) {
            stringBuilder.append(textBlocks[textBlocks.keyAt(i)].value)
        }
        return Response.Success(stringBuilder.toString())
    }
}