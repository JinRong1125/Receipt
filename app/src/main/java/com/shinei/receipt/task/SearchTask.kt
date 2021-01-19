package com.shinei.receipt.task

import com.shinei.receipt.entity.Recipe
import com.shinei.receipt.entity.Response
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchTask {
    companion object {
        private const val API_KEY = "c96a31a3486f452a8c3b930208cffc9b"
    }

    private fun convertURL(ingredients: String) =
        URL("https://api.spoonacular.com/recipes/findByIngredients?apiKey=$API_KEY&ingredients=$ingredients")

    operator fun invoke(ingredients: String): Response<List<Recipe>> {
        (convertURL(ingredients).openConnection() as? HttpURLConnection)?.run {
            return try {
                connect()
                val bufferReader = BufferedReader(InputStreamReader(inputStream))
                val stringBuilder = StringBuilder()
                var currentLine: String?
                while (bufferReader.readLine().also { currentLine = it } != null) {
                    stringBuilder.append(currentLine)
                }
                bufferReader.close()
                disconnect()
                val jsonArray = JSONArray(stringBuilder.toString())
                val recipes = ArrayList<Recipe>()
                for (i in 0 until jsonArray.length()) {
                    jsonArray.getJSONObject(i).let {
                        recipes.add(
                            Recipe(
                                it.getLong("id"),
                                it.getString("title"),
                                it.getString("image")
                            )
                        )
                    }
                }
                Response.Success(recipes)
            } catch (fileNotFoundException: FileNotFoundException) {
                Response.Error(fileNotFoundException)
            } catch (ioException: IOException) {
                Response.Error(ioException)
            } catch (jsonException: JSONException) {
                Response.Error(jsonException)
            }
        }
        return Response.Error(Exception("Cannot open HttpURLConnection"))
    }
}