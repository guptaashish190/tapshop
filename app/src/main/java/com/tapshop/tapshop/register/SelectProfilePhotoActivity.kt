package com.tapshop.tapshop.register

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.squareup.picasso.Picasso
import com.tapshop.tapshop.R
import com.tapshop.tapshop.user.ProfileActivity
import kotlinx.android.synthetic.main.activity_select_profile_photo.*
import java.io.File

class SelectProfilePhotoActivity : AppCompatActivity() {

    companion object {
        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_profile_photo)

        skip_button.setOnClickListener {
            val profileIntent = Intent(this, ProfileActivity::class.java)
            startActivity(profileIntent)
        }

        upload_pp_gallery_button.setOnClickListener {
            selectImageInAlbum()
        }
        upload_pp_camera_button.setOnClickListener {
            takePhoto()
        }
    }
    private fun selectImageInAlbum() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_SELECT_IMAGE_IN_ALBUM)
        }
    }
    private fun takePhoto() {
        val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent1.resolveActivity(packageManager) != null) {
            startActivityForResult(intent1, REQUEST_TAKE_PHOTO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if((requestCode == REQUEST_SELECT_IMAGE_IN_ALBUM || requestCode == REQUEST_TAKE_PHOTO)&& resultCode == Activity.RESULT_OK && data != null){
            val selectedImageUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            pp_image_view.setImageURI(selectedImageUri)
        }
    }
}

