package com.example.jojoapp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.jojoapp.R
import com.example.jojoapp.beans.Character
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EditActivity: AppCompatActivity() {

    private lateinit var detailed_data: Character
    private var isEditing: Boolean=true

    private lateinit var saveButton: Button
    private lateinit var avatarImage: ImageView
    private lateinit var nameTextEdit: EditText
    private lateinit var standEditText: EditText
    private lateinit var ageTextEdit: EditText
    private lateinit var seasonEditText: EditText
    private lateinit var latitudeEditText: EditText
    private lateinit var longitudeEditText: EditText
    private lateinit var descEditText: TextInputEditText

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editview)
    }
}