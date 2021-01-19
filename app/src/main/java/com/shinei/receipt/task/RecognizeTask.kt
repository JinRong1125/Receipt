package com.shinei.receipt.task

import android.content.Context
import android.graphics.Bitmap
import androidx.core.util.forEach
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.text.TextRecognizer
import com.shinei.receipt.entity.Response

class RecognizeTask {

    operator fun invoke(context: Context, bitmap: Bitmap): Response<String> {
        val textBlocks = TextRecognizer.Builder(context).build()
            .detect(Frame.Builder().setBitmap(bitmap).build())
        val texts = ArrayList<String>()
        textBlocks.forEach { key, _ ->
            texts.add(textBlocks[key].value)
        }
        return Response.Success(texts.joinToString(separator = ","))
    }
}