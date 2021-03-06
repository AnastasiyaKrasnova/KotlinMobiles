package com.example.jojoapp.activities

import android.app.Activity
import android.content.Intent
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
import com.example.jojoapp.helpers.Settings
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class DetailedActivity : AppCompatActivity() {

    private lateinit var detailed_data: Character
    private  var document_id: String?=null
    private var settings: Settings = Settings()

    private lateinit var name: TextView
    private lateinit var stand: TextView
    private lateinit var age: TextView
    private lateinit var season: TextView
    private lateinit var avatar: ImageView
    private lateinit var desc: TextInputEditText
    private lateinit var bottomNav: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detailedview)

        name=findViewById(R.id.detailedName)
        stand=findViewById(R.id.detailedStand)
        age=findViewById(R.id.detailedAge)
        season=findViewById(R.id.detailedSeason)
        avatar=findViewById(R.id.detailedImage)
        desc=findViewById(R.id.detailedDescription)
        bottomNav=findViewById(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

    }

    override fun onStart() {
        super.onStart()
        detailed_data=intent.getSerializableExtra("detailed_data") as Character
        document_id=intent.getStringExtra("document_id")
        name.text=detailed_data.name
        stand.text=detailed_data.stand
        age.text=detailed_data.age
        season.text=detailed_data.season
        desc.text=detailed_data.description!!.toEditable()

        settings.getLocale(this)
        settings.getMode(this)
        settings.setTextViewSettings(name, this)
        settings.setTextViewSettings(stand, this)
        settings.setTextViewSettings(age, this)
        settings.setTextViewSettings(season, this)

        loadPicture(detailed_data.avatar!!,avatar,this)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.back -> {
                val refresh = Intent(
                    this,
                    TableViewActivity::class.java
                )
                startActivity(refresh)
                return@OnNavigationItemSelectedListener true
            }
            R.id.photo_button -> {
                var intent= Intent(this, PhotoActivity::class.java)
                intent.putExtra("detailed_data", detailed_data)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.edit_button-> {
                var intent= Intent(this, EditActivity::class.java)
                intent.putExtra("is_editing", true)
                intent.putExtra("detailed_data", detailed_data)
                intent.putExtra("document_id", document_id)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
}