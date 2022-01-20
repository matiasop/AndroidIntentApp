package com.example.otherintentapp

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    val REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Display text sent from other app
        val dataVal: String? = intent.getStringExtra("data")
        val textToDisplay = findViewById<View>(R.id.textToDisplay) as TextView
        textToDisplay.text = dataVal

        val btnReturn = findViewById<View>(R.id.btnReturn)
        btnReturn.setOnClickListener {
            sendDataToOtherApp(otherAppPackageName="com.example.alizarol_android_app", name="image", value="Hi from other app")
//            val otherAppPackageName = "com.example.alizarol_android_app"
//            val otherAppIntent = packageManager.getLaunchIntentForPackage(otherAppPackageName)
//
//            if (otherAppIntent != null) {
//                // Add additional data to intent
//                otherAppIntent.putExtra("data", "Hi from other app")
//                // Start other app
//                startActivity(otherAppIntent)
//            } else {
//                Toast.makeText(this, "Couldn't open other app", Toast.LENGTH_LONG)
//            }
        }
        // Open Image Gallery
        val imgButton = findViewById<View>(R.id.imgButton)
        imgButton.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            gallery.type = "image/*"
            startActivityForResult(gallery, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            val imageView: ImageView = findViewById<View>(R.id.imageView) as ImageView
            imageView.setImageURI(data?.data) // handle chosen image
        }

        // get Real image path
        val imageUri: Uri? = data?.data
        val imagePath: String? = imageUri?.let { getRealPathFromURI(it) }

        if (imagePath != null) {
            sendDataToOtherApp(otherAppPackageName="com.example.alizarol_android_app", name="image", value=imagePath)
        }
    }

    private fun sendDataToOtherApp(otherAppPackageName: String, name: String, value: String) {
        val otherAppIntent = packageManager.getLaunchIntentForPackage(otherAppPackageName)

        if (otherAppIntent != null) {
            // Add additional data to intent
            otherAppIntent.putExtra(name, value)
            // Start other app
            startActivity(otherAppIntent)
        } else {
            Toast.makeText(this, "Couldn't open other app", Toast.LENGTH_LONG).show()
        }
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String
        val cursor: Cursor? = contentResolver.query(contentURI, null, null, null, null)

        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath().toString()
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }
}
