package com.tapshop.tapshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var username: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var repassword: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button.setOnClickListener {
            username = username_textinput.text.toString()
            password = password_textinput.text.toString()
            repassword = re_password_textinput.text.toString()
            email = email_textinput.text.toString()
            if(validateUserInput()){
                val auth = FirebaseAuth.getInstance()
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){
                        if(it.isSuccessful){
                            Log.d("Register", "Registered")
                            Toast.makeText(baseContext, "Registered User",
                                Toast.LENGTH_SHORT).show()
                            finish()
                        }else{
                            // Cancel Display error{
                            Log.d("Register", "createUserWithEmail:failure")
                        }
                    }
            }else{
                println("Nopeee")
            }
        }

        setTextChangeListener(username_textinput, username_layout)
        setTextChangeListener(password_textinput, password_layout)
        setTextChangeListener(re_password_textinput, re_password_layout)
        setTextChangeListener(email_textinput, email_layout)

    }

    private fun setTextChangeListener(editText: TextInputEditText, textInputLayout: TextInputLayout){
        editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textInputLayout.error = null
            }
        })
    }

    private fun validateUserInput(): Boolean{
        var valid = true

        username_layout.error = null
        password_layout.error = null
        re_password_layout.error = null

        if(email.isEmpty()){
            email_layout.error = "Provide an email"
            valid = false
        }

        if(username.isEmpty()){
            username_layout.error = "Provide a username"
            valid = false
        }
        if(password.isEmpty()){
            password_layout.error = "Provide a password"
            valid = false
        }
        if(password.length < 6){
            password_layout.error = "Password Length should be atleast 6"
            valid = false
        }
        if(repassword.isEmpty()){
            re_password_layout.error = "Re-Enter the password"
            valid = false
        }

        if(password != repassword){
            re_password_layout.error = "Passwords do not match"
            valid = false
        }

        return(valid)
    }
}
