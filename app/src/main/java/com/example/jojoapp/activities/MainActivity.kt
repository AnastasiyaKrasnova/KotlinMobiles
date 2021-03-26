package com.example.jojoapp.activities

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.jojoapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var tumb: ImageView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        auth = Firebase.auth
        loginButton=findViewById(R.id.signupButton)
        registerButton=findViewById(R.id.registerButton)
        emailEditText=findViewById(R.id.emailTextField)
        passwordEditText=findViewById(R.id.passwordTextField)
        tumb=findViewById(R.id.tumb)
        loginButton.setOnClickListener{
            LoginUser(emailEditText.text.trim().toString(),passwordEditText.text.trim().toString())
        }

        registerButton.setOnClickListener{
            var intent= Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        val bm = retrieveVideoFrameFromVideo("https://firebasestorage.googleapis.com/v0/b/customlogindemo-b3873.appspot.com/o/images%2FDio.mp4?alt=media&token=38ae8172-28cf-4aff-96b2-bb94ed34fa6f")
        tumb.setImageBitmap(bm)

    }

    private fun validateFields (): String?{
        if (emailEditText.text.trim().toString().isNotEmpty() && passwordEditText.text.trim().toString().isNotEmpty()){
            return null;
        }
        else return "Fields cant be empty"
    }

    private fun LoginUser(email: String?,password: String?){
        if (validateFields()!=null){
            Toast.makeText(baseContext, "Fill all text fields",
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
                            Toast.makeText(baseContext, "Email or password is incorrect",
                                    Toast.LENGTH_SHORT).show()
                        }
                    }
        }


    }

    fun retrieveVideoFrameFromVideo(videoPath: String?): Bitmap? {
        var bitmap: Bitmap? = null
        var mediaMetadataRetriever: MediaMetadataRetriever? = null
        try {
            mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(videoPath, HashMap<String, String>())
            bitmap = mediaMetadataRetriever.frameAtTime
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mediaMetadataRetriever?.release()
        }
        return bitmap
    }

}