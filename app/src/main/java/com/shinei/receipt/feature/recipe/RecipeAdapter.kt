package com.shinei.receipt.feature.recipe

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shinei.receipt.R
import com.shinei.receipt.entity.Recipe
import com.shinei.receipt.entity.Response
import com.shinei.receipt.task.LoadImageTask
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class RecipeAdapter(private val context: Context, private var recipes: List<Recipe>) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private val executorService = Executors.newFixedThreadPool(2)
    private val loadImageTask by lazy { LoadImageTask() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_recipe, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position], executorService, loadImageTask)
    }

    override fun onViewDetachedFromWindow(holder: RecipeViewHolder) {
        holder.detach()
        super.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount() = recipes.size

    fun refresh(recipes: List<Recipe>) {
        this.recipes = recipes
        notifyDataSetChanged()
    }

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var bitmap: Bitmap? = null
        private var future: Future<*>? = null

        fun bind(recipe: Recipe, executorService: ExecutorService, loadImageTask: LoadImageTask) {
            val image = itemView.findViewById<ImageView>(R.id.image)
            val name = itemView.findViewById<TextView>(R.id.name)
            if (bitmap != null) {
                image.setImageBitmap(bitmap)
            } else {
                future = executorService.submit {
                    loadImageTask(recipe.image).let {
                        if (it is Response.Success) {
                            this.bitmap = it.data
                            image.setImageBitmap(it.data)
                        }
                    }
                }
            }
            name.text = recipe.title
        }

        fun detach() {
            future?.cancel(true)
        }
    }
}