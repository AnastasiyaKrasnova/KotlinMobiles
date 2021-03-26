package com.example.jojoapp.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.jojoapp.R
import com.example.jojoapp.beans.Character
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class DetailedActivity : AppCompatActivity() {

    private lateinit var detailed_data: Character

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.detailedview)
        }

    override fun onStart() {
            super.onStart()
            detailed_data=intent.getSerializableExtra("detailed_data") as Character
            Log.d("TAG", "${detailed_data.doc_id} => ${detailed_data.name}")
        }
}