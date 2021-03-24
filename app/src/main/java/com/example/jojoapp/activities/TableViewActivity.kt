package com.example.jojoapp.activities

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.jojoapp.R
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.InputStream

@GlideModule
class AppGlide: AppGlideModule(){

    override fun registerComponents(
        context: android.content.Context,
        glide: Glide,
        registry: Registry
    ) {
        super.registerComponents(context, glide, registry)
        registry.append(
            StorageReference::class.java, InputStream::class.java,
            FirebaseImageLoader.Factory()
        )

    }
}

class TableViewActivity : AppCompatActivity() {

    public lateinit var characterList: QuerySnapshot
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tableview)
        storage = Firebase.storage
        loadPicture()
    }

    override fun onStart() {
        super.onStart()
        getCharacters()
    }

    private fun getCharacters(){
        val db = Firebase.firestore
        db.collection("characters")
            .get()
            .addOnSuccessListener { result ->
                characterList=result
                for (document in result) {
                    Log.d("TAG", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents: ", exception)
            }
    }

    private fun loadPicture(){
        val httpsReference = storage.getReferenceFromUrl(
            "https://firebasestorage.googleapis.com/v0/b/customlogindemo-b3873.appspot.com/o/images%2FGiorno.jpg?alt=media&token=ff8dc693-07d8-4b64-a3d4-2867ed2ebbe5")
        val imageView = findViewById<ImageView>(R.id.myImage)
        GlideApp.with(this)
            .load(httpsReference)
            .into(imageView)
    }
}
