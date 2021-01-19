package com.shinei.receipt.feature.camera

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.shinei.receipt.R
import com.shinei.receipt.feature.recipe.RecipeActivity

class CameraActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 0
    }

    private lateinit var cameraModel: CameraModel
    private lateinit var buttonCard: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        setup()
    }

    private fun setup() {
        cameraModel = CameraModel(object : CameraContract {
            override fun onRecognized(text: String) {
                startActivity(
                    Intent(this@CameraActivity, RecipeActivity::class.java).putExtra(
                        RecipeActivity.ARGUMENT_TEXT, text
                    )
                )
            }
        })
        buttonCard = findViewById(R.id.buttonCard)
        buttonCard.setOnClickListener {
            takePicture()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            (data?.extras?.get("data") as? Bitmap)?.let {
                cameraModel.recognize(this, it)
            }
        }
    }

    private fun takePicture() {
        try {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_IMAGE_CAPTURE)
        } catch (activityNotFoundException: ActivityNotFoundException) {
        }
    }
}