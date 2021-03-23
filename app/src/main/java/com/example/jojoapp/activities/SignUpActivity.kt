package com.example.jojoapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import com.example.jojoapp.R
import com.example.jojoapp.beans.User
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
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        auth = Firebase.auth

        emailEditText=findViewById(R.id.emailTextField)
        passwordEditText=findViewById(R.id.passwordTextField)
        firstnameEditText=findViewById(R.id.firstnameTextField)
        lastnameEditText=findViewById(R.id.lastnameTextField)
        signUpButton=findViewById(R.id.signupButton)

        signUpButton.setOnClickListener{
            Register(firstnameEditText.text.trim().toString(), lastnameEditText.text.trim().toString(),
                    emailEditText.text.trim().toString(), passwordEditText.text.trim().toString())
        }
    }

    private fun validateFields (): String?{
        if (emailEditText.text.trim().toString().isNotEmpty() && passwordEditText.text.trim().toString().isNotEmpty()
                && firstnameEditText.text.trim().toString().isNotEmpty() && lastnameEditText.text.trim().toString().isNotEmpty()){
            return null;
        }
        else return "Fields cant be empty"
    }

    private fun Register(firstname: String?, lastname: String?, email: String?, password: String?){
        if (validateFields()!=null){
            Toast.makeText(baseContext, "Fill all text fields",
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
                                        Toast.makeText(baseContext, "Unable to create user",
                                                Toast.LENGTH_SHORT).show()
                                        Log.w("TAG", "Error adding document", e)
                                    }
                        } else {
                            Toast.makeText(baseContext, "User with such email already exists",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }
}