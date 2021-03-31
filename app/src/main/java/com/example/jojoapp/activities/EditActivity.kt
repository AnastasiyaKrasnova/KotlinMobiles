package com.example.jojoapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jojoapp.R
import com.example.jojoapp.beans.Character
import com.example.jojoapp.dao.loadPicture
import com.example.jojoapp.dao.storePicture
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditActivity: AppCompatActivity() {

    private lateinit var detailed_data: Character
    private  var document_id: String?=null
    private var isEditing: Boolean=true
    private var isClicked: Boolean=false

    private val pickImage = 100
    private var imageUri: Uri?=null

    private lateinit var saveButton: Button
    private lateinit var avatarImage: ImageView
    private lateinit var nameTextEdit: EditText
    private lateinit var standEditText: EditText
    private lateinit var ageTextEdit: EditText
    private lateinit var seasonEditText: EditText
    private lateinit var latitudeEditText: EditText
    private lateinit var longitudeEditText: EditText
    private lateinit var descEditText: TextInputEditText
    private lateinit var bottomNav: BottomNavigationView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.editview)

        auth = Firebase.auth

        nameTextEdit=findViewById(R.id.editName)
        standEditText=findViewById(R.id.editStand)
        ageTextEdit=findViewById(R.id.editAge)
        seasonEditText=findViewById(R.id.editSeason)
        avatarImage=findViewById(R.id.editAvatarImage)
        descEditText=findViewById(R.id.editDesc)
        latitudeEditText=findViewById(R.id.editLat)
        longitudeEditText=findViewById(R.id.editLon)
        saveButton=findViewById(R.id.editButton)
        bottomNav=findViewById(R.id.bottom_navigation)

        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        saveButton.setOnClickListener{
            DispatchAction(nameTextEdit.text.trim().toString(), standEditText.text.trim().toString(),
                    ageTextEdit.text.trim().toString(),
                    seasonEditText.text.trim().toString(),
                    latitudeEditText.text.trim().toString(),
                    longitudeEditText.text.trim().toString(), descEditText.text.toString())
        }

        avatarImage.setOnClickListener{
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            gallery.type="image/*"
            startActivityForResult(gallery, pickImage)
        }
    }

    override fun onStart() {
        super.onStart()
        isEditing=intent.getBooleanExtra("is_editing",true)
        if (isEditing){
            detailed_data=intent.getSerializableExtra("detailed_data") as Character
            document_id=intent.getStringExtra("document_id")
            nameTextEdit.text=detailed_data.name!!.toEditable()
            standEditText.text=detailed_data.stand!!.toEditable()
            ageTextEdit.text=detailed_data.age!!.toEditable()
            seasonEditText.text=detailed_data.season!!.toEditable()
            descEditText.text=detailed_data.description!!.toEditable()
            latitudeEditText.text=detailed_data.latitude.toString().toEditable()
            longitudeEditText.text=detailed_data.longitude.toString().toEditable()
            loadPicture(detailed_data.avatar!!,avatarImage,this)
        }
    }

    private fun validateFields (): String?{
        if (nameTextEdit.text.trim().toString().isNotEmpty() && standEditText.text.trim().toString().isNotEmpty()
                && ageTextEdit.text.trim().toString().isNotEmpty()
                && seasonEditText.text.trim().toString().isNotEmpty()
                && latitudeEditText.text.trim().toString().isNotEmpty()
                && longitudeEditText.text.trim().toString().isNotEmpty()){

            val x=latitudeEditText.text.trim().toString().toDoubleOrNull()
            val y=longitudeEditText.text.trim().toString().toDoubleOrNull()
            if ((x!=null && (x > -89.3 && x< 89.3)) && (y!=null && (y > -89.3 && y< 89.3))){
                return null
            }
            else return "Coords must be of number value"
        }
        else return "Fill all text fields"
    }

    private fun DispatchAction(name: String?, stand: String?, age: String?, season: String?,
                               latitude: String?, longitude: String?, desc: String?){
        val res=validateFields()
        if (res!=null){
            Toast.makeText(baseContext, res,
                    Toast.LENGTH_SHORT).show()
        }
        else{
            val res=storePicture(avatarImage,getRandomString(6)+".jpg")
            res.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val new_avatar = task.result
                    if (isEditing){
                        EditCharacter(name!!,stand!!,age!!,season!!,latitude!!,longitude!!,desc, new_avatar.toString())
                    }
                    else{
                        AddCharacter(name!!,stand!!,age!!,season!!,latitude!!,longitude!!,desc, new_avatar.toString())
                    }
                } else {
                    Log.e("TAG", "Error occured while loading image")
                }
            }
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

    private fun AddCharacter(name: String, stand: String, age: String?, season: String,
                             latitude: String, longitude: String, desc: String?, avatar: String?){

        var character=Character(name,stand,age,season,avatar,desc,ArrayList<String>(), ArrayList<String>(),latitude,longitude)
        val db = Firebase.firestore
        db.collection("characters")
                .add(character)
                .addOnSuccessListener { documentReference ->
                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                    var intent= Intent(this, TableViewActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(baseContext, "Unable to create user",
                            Toast.LENGTH_SHORT).show()
                    Log.w("TAG", "Error adding document", e)
                }
    }

    private fun EditCharacter(name: String, stand: String, age: String?, season: String,
                             latitude: String, longitude: String, desc: String?, avatar: String?){
        val db = Firebase.firestore
        db.collection("characters").document(document_id!!)
                .update(mapOf(
                        "name" to name, "stand" to stand, "age" to age,"season" to season,
                        "description" to desc,"latitude" to latitude, "longitude" to longitude,
                        "avatar" to avatar
                ))
                .addOnSuccessListener {
                    Log.d("TAG", "DocumentSnapshot successfully updated!")
                    var intent= Intent(this, TableViewActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { e -> Log.w("TAG", "Error updating document", e) }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
                //imageUri = data?.data
                //avatarImage.setImageURI(imageUri)
                //Log.d("TAG", "start")
        }
    }

    fun getRandomString(length: Int) : String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
                .map { charset.random() }
                .joinToString("")
    }

    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)

}