package com.shinei.receipt.feature.recipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shinei.receipt.R
import com.shinei.receipt.entity.Recipe

class RecipeActivity : AppCompatActivity() {

    companion object {
        const val ARGUMENT_TEXT = "argument_text"
    }

    private lateinit var ingredientsText: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var recipeModel: RecipeModel

    private var recipeAdapter: RecipeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)
        setup()
        load()
    }

    private fun setup() {
        ingredientsText = findViewById(R.id.ingredientsText)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = RecipeAdapter(
            this,
            emptyList()
        ).also {
            this.recipeAdapter = it
        }
        recipeModel = RecipeModel(object : RecipeContract {
            override fun onResponse(recipes: List<Recipe>) {
                recipeAdapter?.refresh(recipes)
            }
        })
    }

    private fun load() {
        intent.getStringExtra(ARGUMENT_TEXT)?.let {
            ingredientsText.text = it
            recipeModel.search(it)
        }
    }
}