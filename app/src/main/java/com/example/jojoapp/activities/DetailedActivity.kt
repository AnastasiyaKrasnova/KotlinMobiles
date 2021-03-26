package com.example.jojoapp.activities

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.jojoapp.R
import com.example.jojoapp.beans.Character
import com.example.jojoapp.dao.loadPicture
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class DetailedActivity : AppCompatActivity() {

    private lateinit var detailed_data: Character
    private lateinit var name: TextView
    private lateinit var stand: TextView
    private lateinit var age: TextView
    private lateinit var season: TextView
    private lateinit var avatar: ImageView
    private lateinit var desc: TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailedview)

        name=findViewById(R.id.detailedName)
        stand=findViewById(R.id.detailedStand)
        age=findViewById(R.id.detailedAge)
        season=findViewById(R.id.detailedSeason)
        avatar=findViewById(R.id.detailedImage)
        desc=findViewById(R.id.detailedDescription)

    }

    override fun onStart() {
        super.onStart()
        detailed_data=intent.getSerializableExtra("detailed_data") as Character
        name.text=detailed_data.name
        stand.text=detailed_data.stand
        age.text=detailed_data.age
        season.text=detailed_data.season
        desc.text=detailed_data.description!!.toEditable()
        loadPicture(detailed_data.avatar!!,avatar,this)
    }

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
}