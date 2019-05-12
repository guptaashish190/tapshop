package com.tapshop.tapshop

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.tapshop.tapshop.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {


    private lateinit var username: String
    private lateinit var password: String
    private val RC_SIGN_IN = 0
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        setTextChangeListener(username_textinput, username_layout)
        setTextChangeListener(password_textinput, password_layout)

        configureGoogleSignIn()
        mAuth = FirebaseAuth.getInstance()
        login_button.setOnClickListener {
            username = username_textinput.text.toString()
            password = password_textinput.text.toString()
            if(validateUserPass()){
                mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this){
                    if(it.isSuccessful){
                        Log.d("Login", "signInWithEmail:success")
                        Toast.makeText(baseContext, "Signed In",
                            Toast.LENGTH_SHORT).show()
                    }else{

                        Log.w("Login", "signInWithEmail:failure", it.exception)
                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this,"Invalid", Toast.LENGTH_SHORT).show()
            }
        }

        google_sign_in_button.setOnClickListener{
            signIn()
        }

        new_user_button.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    private fun configureGoogleSignIn(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)
    }
    private fun validateUserPass(): Boolean{
        var valid = true

        if(username.isEmpty()){
            username_layout.error = "Provide a username"
            valid = false
        }
        if(password.isEmpty()){
            password_layout.error = "Provide a password"
            valid = false
        }

        return(valid)
    }

    private fun signIn(){
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun setTextChangeListener(editText: TextInputEditText, textInputLayout: TextInputLayout): Unit{
        editText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textInputLayout.error = null
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login", "Google sign in failed", e)
                // ...
            }
        }
    }
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount){
        Log.d("Login", "firebaseAuthWithGoogle:" + account.id!!)
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener(this){
            if(it.isSuccessful){
                Log.d("Login", "signInWithCredential:success")
                Toast.makeText(this,"Auth Success",Toast.LENGTH_LONG).show()
            }else{
                Log.w("Login", "signInWithCredential:failure", it.exception)
                Toast.makeText(this,"Auth Failed",Toast.LENGTH_LONG).show()
            }
        }
    }

}
