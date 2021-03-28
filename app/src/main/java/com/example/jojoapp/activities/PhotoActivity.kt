package com.example.jojoapp.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.jojoapp.R
import com.example.jojoapp.beans.Character
import com.example.jojoapp.dao.loadPicture
import com.example.jojoapp.dao.retrieveVideoFrameFromVideo
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlin.properties.Delegates

class PhotoActivity: AppCompatActivity() {

    private lateinit var detailed_data: Character
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var collection: GridView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photoview)
        bottomNav=findViewById(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        detailed_data=intent.getSerializableExtra("detailed_data") as Character

        collection=findViewById<GridView>(R.id.photoCollection)
        var customAdapter=PhotoCustomAdapter(detailed_data,this)
        collection.adapter=customAdapter

        collection.setOnItemClickListener { adapterview: AdapterView<*>?, view: View?, position: Int, id: Long ->
            if (position>=detailed_data.photo!!.size){
                var intent= Intent(this, PlayerActivity::class.java)
                intent.putExtra("video_url", detailed_data.video!![position-detailed_data.photo!!.size])
                startActivity(intent)
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
}

class PhotoCustomAdapter(var characterModel: Character, var context: Context):
    BaseAdapter(){

    var border=0
    var layoutInflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        var row_view=view
        if (row_view==null){
            row_view=layoutInflater.inflate(R.layout.photo_item,viewGroup,false )
        }
        var photo=row_view?.findViewById<ImageView>(R.id.photoImage)
        FillControllers(photo!!, position, characterModel.photo, characterModel.video)
        return row_view!!
    }

    override fun getItem(position: Int): Any {
        border=characterModel.photo!!.size
        if (position<border)
            return characterModel.photo!![position]
        else
            return characterModel.video!![position-border]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return characterModel.photo!!.size+characterModel.video!!.size
    }

    private fun FillControllers(image: ImageView, position: Int, photo_array: ArrayList<String>?, video_array: ArrayList<String>?){
        if (position < characterModel.photo!!.size) {
            loadPicture(photo_array!![position], image, context as Activity)
        }
        else{
            val bm = retrieveVideoFrameFromVideo(video_array!![position-characterModel.photo!!.size])
            image.setImageBitmap(bm)
        }
    }
}