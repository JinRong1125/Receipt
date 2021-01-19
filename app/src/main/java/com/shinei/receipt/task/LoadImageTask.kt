package com.shinei.receipt.task

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.shinei.receipt.entity.Response
import java.net.HttpURLConnection
import java.net.URL

class LoadImageTask {

    operator fun invoke(url: String): Response<Bitmap> {
        (URL(url).openConnection() as? HttpURLConnection)?.run {
            return try {
                connect()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                disconnect()
                Response.Success(bitmap)
            } catch (exception: Exception) {
                Response.Error(exception)
            }
        }
        return Response.Error(Exception("Cannot download image"))
    }
}