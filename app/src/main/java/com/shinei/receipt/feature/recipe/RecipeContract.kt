package com.shinei.receipt.feature.recipe

import com.shinei.receipt.entity.Recipe

interface RecipeContract {
    fun onResponse(recipes: List<Recipe>)
}