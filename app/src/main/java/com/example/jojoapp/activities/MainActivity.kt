package com.example.jojoapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.jojoapp.R
import com.example.jojoapp.helpers.Settings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.yandex.mapkit.MapKitFactory


class MainActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    private lateinit var auth: FirebaseAuth
    private var settings: Settings = Settings()

    override fun onStart() {
        super.onStart()
        settings.getLocale(this)
        settings.getMode(this)
        settings.setButtonSettings(loginButton,this)
        settings.setButtonSettings(registerButton,this)
        settings.setTextEditSettings(emailEditText,this)
        settings.setTextEditSettings(passwordEditText,this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginview)

        settings.getLocale(this)
        auth = Firebase.auth
        loginButton=findViewById(R.id.signupButton)
        registerButton=findViewById(R.id.registerButton)
        emailEditText=findViewById(R.id.emailTextField)
        passwordEditText=findViewById(R.id.passwordTextField)
        loginButton.setOnClickListener{
            LoginUser(emailEditText.text.trim().toString(),passwordEditText.text.trim().toString())
        }

        registerButton.setOnClickListener{
            var intent= Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    private fun validateFields (): String?{
        if (emailEditText.text.trim().toString().isNotEmpty() && passwordEditText.text.trim().toString().isNotEmpty()){
            return null;
        }
        else return getString(R.string.login_fields_empty)
    }

    private fun LoginUser(email: String?,password: String?){
        if (validateFields()!=null){
            Toast.makeText(baseContext, getString(R.string.login_fill_text),
                    Toast.LENGTH_SHORT).show()
        }
        else{
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d("TAG", "signInWithEmail:success")
                            var intent= Intent(this, TableViewActivity::class.java)
                            startActivity(intent)
                        } else {
                            Log.d("TAG", "signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, R.string.login_password_incorrect,
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
        }


    }

}