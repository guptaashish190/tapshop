package com.tapshop.tapshop.register

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.tapshop.tapshop.R
import com.tapshop.tapshop.models.UserModel
import com.tapshop.tapshop.user.ProfileActivity
import kotlinx.android.synthetic.main.activity_select_profile_photo.*
import java.io.File
import java.util.*

class SelectProfilePhotoActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_TAKE_PHOTO = 0
        const val REQUEST_SELECT_IMAGE_IN_ALBUM = 1
    }

    private lateinit var imageUri: Uri
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

        next_button.setOnClickListener {
                uploadPPToFirebaseAndSaveUser(this.imageUri)
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
            this.imageUri = data.data!!
            pp_image_view.setImageURI(this.imageUri)
            next_button.isEnabled = true
        }
    }

    private fun uploadPPToFirebaseAndSaveUser(uri: Uri?){
        val uid = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/profileimages/$uid")
        ref.putFile(uri!!).addOnSuccessListener {
            Log.d("SelectPPActivity", "Successfully uploaded photo ${it.metadata?.path}")

            ref.downloadUrl.addOnSuccessListener{url ->
                Log.d("SelectPPActivity", "Profile Picture Url is : $url")
                saveUserToDatabase(url.toString())
            }
        }
    }

    private fun saveUserToDatabase(url: String){
        val database = FirebaseDatabase.getInstance()
        val uid = intent.getStringExtra(RegisterActivity.UID)
        val ref = database.getReference("/users/$uid")

        // userinfo
        val username = intent.getStringExtra(RegisterActivity.USERNAME)
        val gender = intent.getStringExtra(RegisterActivity.GENDER)
        val friends = emptyList<String>()
        val user = UserModel(username,url,gender,friends)

        ref.setValue(user).addOnSuccessListener {
            Log.d("SelectPPActivity", "Successfully Saved user to db")
        }
    }

}

