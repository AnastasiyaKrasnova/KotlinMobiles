package com.example.jojoapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import com.example.jojoapp.R
import com.example.jojoapp.beans.User
import com.example.jojoapp.helpers.Settings
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore



class SignUpActivity : AppCompatActivity(){

    private lateinit var firstnameEditText: EditText
    private lateinit var lastnameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signUpButton: Button
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var auth: FirebaseAuth

    private var settings: Settings = Settings()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signupview)
        auth = Firebase.auth

        emailEditText=findViewById(R.id.emailTextField)
        passwordEditText=findViewById(R.id.passwordTextField)
        firstnameEditText=findViewById(R.id.firstnameTextField)
        lastnameEditText=findViewById(R.id.lastnameTextField)
        signUpButton=findViewById(R.id.signupButton)
        bottomNav=findViewById(R.id.bottom_navigation)

        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        signUpButton.setOnClickListener{
            Register(firstnameEditText.text.trim().toString(), lastnameEditText.text.trim().toString(),
                    emailEditText.text.trim().toString(), passwordEditText.text.trim().toString())
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.back -> {
                finish()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun validateFields (): String?{
        if (emailEditText.text.trim().toString().isNotEmpty() && passwordEditText.text.trim().toString().isNotEmpty()
                && firstnameEditText.text.trim().toString().isNotEmpty() && lastnameEditText.text.trim().toString().isNotEmpty()){
            return null;
        }
        else return getString(R.string.register_fields_empty)
    }

    private fun Register(firstname: String?, lastname: String?, email: String?, password: String?){

        val res=validateFields()
        if (res!=null){
            Toast.makeText(baseContext, res,
                    Toast.LENGTH_SHORT).show()
        }
        else{
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val db = Firebase.firestore
                            var user=User(firstname,lastname,email,null)
                            db.collection("users")
                                    .add(user)
                                    .addOnSuccessListener { documentReference ->
                                        Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                                        var intent= Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(baseContext, getString(R.string.register_unable_to_create),
                                                Toast.LENGTH_SHORT).show()
                                        Log.w("TAG", "Error adding document", e)
                                    }
                        } else {
                            Toast.makeText(baseContext, getString(R.string.register_user_exists),
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }

    override fun onStart() {
        super.onStart()
        settings.getLocale(this)
        settings.setButtonSettings(signUpButton,this)
        settings.setTextEditSettings(firstnameEditText,this)
        settings.setTextEditSettings(lastnameEditText,this)
        settings.setTextEditSettings(emailEditText,this)
        settings.setTextEditSettings(passwordEditText,this)
        settings.getMode(this)
    }
}