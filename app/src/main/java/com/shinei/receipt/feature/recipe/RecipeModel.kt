package com.shinei.receipt.feature.recipe

import android.os.Handler
import android.os.Looper
import androidx.core.os.HandlerCompat
import com.shinei.receipt.entity.Response
import com.shinei.receipt.task.SearchTask
import java.util.concurrent.Executors

class RecipeModel(private val recipeContract: RecipeContract) {

    private val executorService = Executors.newSingleThreadExecutor()
    private val mainThreadHandler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())

    private val searchTask by lazy { SearchTask() }

    fun search(ingredients: String) {
        executorService.execute {
            searchTask(ingredients).let {
                if (it is Response.Success) {
                    mainThreadHandler.post {
                        recipeContract.onResponse(it.data)
                    }
                }
            }
        }
    }
}