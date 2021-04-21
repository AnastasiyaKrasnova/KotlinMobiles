package com.example.jojoapp.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.jojoapp.R
import com.example.jojoapp.beans.CharacterList
import com.example.jojoapp.helpers.Settings
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CircleMapObject
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider


class MapActivity: AppCompatActivity() {

    private lateinit var data: ArrayList<com.example.jojoapp.beans.Character>
    private lateinit var documents: ArrayList<String>
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var mapview: MapView
    private lateinit var mapObjects: MapObjectCollection

    private val LOCATION_PERMS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    private val INITIAL_REQUEST = 1337
    private val LOCATION_REQUEST = INITIAL_REQUEST + 3

    private var settings: Settings = Settings()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.yandex_mapview)
        mapview = findViewById<View>(R.id.mapview) as MapView

        if (canAccessLocation()){
        }
        else
        {
            ActivityCompat.requestPermissions(this,LOCATION_PERMS, LOCATION_REQUEST)
        }
        bottomNav=findViewById(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        mapview.map.mapObjects
                .addTapListener { mapObject, point ->
                    var intent= Intent(this, DetailedActivity::class.java)
                    intent.putExtra("detailed_data", (mapObject.userData as CustomUserData).character)
                    intent.putExtra("document_id", (mapObject.userData as CustomUserData).document_id)
                    startActivity(intent)
                    true
                }
        mapObjects = mapview.getMap().getMapObjects().addCollection()
               
    }

    private fun canAccessLocation(): Boolean {
        return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun hasPermission(perm: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this,perm)
    }

    override fun onStop() {
        super.onStop()
        mapview.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()

        mapview.onStart()
        MapKitFactory.getInstance().onStart()
        data=(intent.getSerializableExtra("character_data") as CharacterList).characters
        documents=intent.getStringArrayListExtra("documents") as ArrayList<String>

        for (i in 0..data.count()-1){
            createMapPoint(data[i], documents[i])
        }
    }

    private fun customTapListener(){

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
        }
        false
    }

    private fun Context.getBitmapFromVectorDrawable(drawableId: Int): Bitmap? {
        var drawable = ContextCompat.getDrawable(     this, drawableId) ?: return null

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable).mutate()
        }

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888) ?: return null
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    private fun createMapPoint(character: com.example.jojoapp.beans.Character, doc_id: String){
        Log.d("TAG", "${character.latitude} => ${character.longitude}=> ${character.name}")
        var point=Point(character.latitude!!.toDouble(), character.longitude!!.toDouble())
        val mark = mapObjects.addPlacemark(point)
        val bitmap = this.getBitmapFromVectorDrawable(R.drawable.ic_mappin)
        mark.setIcon(ImageProvider.fromBitmap(bitmap))
        mark.userData=CustomUserData(doc_id, character)
    }
}

class CustomUserData{

    public lateinit var character: com.example.jojoapp.beans.Character
    public lateinit var document_id: String

    constructor(_doc_id: String, _character: com.example.jojoapp.beans.Character){

        document_id=_doc_id
        character=_character
    }
}
