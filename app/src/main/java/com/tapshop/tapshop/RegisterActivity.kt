package com.tapshop.tapshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button.setOnClickListener {
            val username: String = username_textinput.text.toString()
            val pass:String = password_textinput.text.toString()
            val repass: String = re_password_textinput.text.toString()
            val email: String = email_textinput.text.toString()
            if(validateUserInput(username, pass, repass, email)){
                val auth = FirebaseAuth.getInstance()
                auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this){
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

    private fun setTextChangeListener(editText: TextInputEditText, textInputLayout: TextInputLayout): Unit{
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

    private fun validateUserInput( username: String,  password: String, repassword: String, email: String): Boolean{
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
