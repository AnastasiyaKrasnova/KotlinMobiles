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
import com.example.jojoapp.beans.CharacterList
import com.example.jojoapp.dao.loadPicture
import com.example.jojoapp.helpers.Settings
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class TableViewActivity : AppCompatActivity() {

    private lateinit var characterList: QuerySnapshot
    private lateinit var collection: GridView
    private lateinit var bottomNav: BottomNavigationView
    private var customAdapter: CharacterCustomAdapter?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tableview)

        bottomNav=findViewById(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        collection=findViewById<GridView>(R.id.characterCollection)

        collection.setOnItemClickListener{ adapterview: AdapterView<*>?, view: View?, position: Int, id: Long ->

            if (view!!.id == R.id.button_close_item) {
                deleteCharacter(characterList.documents[position].id)
                var intent= Intent(this, TableViewActivity::class.java)
                startActivity(intent)

            }
            else if (view!!.id == R.id.button_details){
                var character=RepackCharacterData(position)
                var intent= Intent(this, DetailedActivity::class.java)
                intent.putExtra("detailed_data", character)
                intent.putExtra("document_id", characterList.documents[position].id)
                startActivity(intent)
            }

        }
    }

    override fun onStart() {
        super.onStart()
        getCharacters()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.back -> {
                val refresh = Intent(
                    this,
                    MainActivity::class.java
                )
                startActivity(refresh)
                return@OnNavigationItemSelectedListener true
            }
            R.id.add_button-> {
                var intent= Intent(this, EditActivity::class.java)
                intent.putExtra("is_editing", false)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.settings_button-> {
                var intent= Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }

            R.id.map_button-> {

                var data=arrayListOf<Character>()
                var documents=arrayListOf<String>()
                for(i in 0..characterList.count()-1){
                    data.add(RepackCharacterData(i))
                    documents.add(characterList.documents[i].id)
                }

                var characters=CharacterList(data)

                var intent= Intent(this, MapActivity::class.java)
                intent.putExtra("character_data", characters)
                intent.putExtra("documents", documents)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun getCharacters() {
        val db = Firebase.firestore
        db.collection("characters")
            .get()
            .addOnSuccessListener { result ->
                characterList=result
                customAdapter=CharacterCustomAdapter(characterList,this, Intent(this, TableViewActivity::class.java))
                collection.adapter=customAdapter
                for (document in result) {
                    Log.d("TAG", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents: ", exception)
            }
    }

    private fun deleteCharacter(doc_id: String){
        val db = Firebase.firestore
        db.collection("characters").document(doc_id)
            .delete()
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully deleted!") }
            .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }
    }

    private fun RepackCharacterData(position: Int): Character{
        var character=Character(
                characterList.documents[position].data?.get("name") as String?,
                characterList.documents[position].data?.get("stand") as String?,
                characterList.documents[position].data?.get("age") as String?,
                characterList.documents[position].data?.get("season") as String?,
                characterList.documents[position].data?.get("avatar") as String?,
                characterList.documents[position].data?.get("description") as String?,
                characterList.documents[position].data?.get("images") as ArrayList<String>?,
                characterList.documents[position].data?.get("videos") as ArrayList<String>?,
                characterList.documents[position].data?.get("latitude") as String?,
                characterList.documents[position].data?.get("longitude") as String?
        )
        return character;
    }
}

class CharacterCustomAdapter(var itemModel: QuerySnapshot, var context: Context, var intent: Intent):
    BaseAdapter(){
    private var settings: Settings=Settings()
    var layoutInflater=context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        var row_view=view
        if (row_view==null){
            row_view=layoutInflater.inflate(R.layout.row_item,viewGroup,false )
        }
        var avatar=row_view?.findViewById<ImageView>(R.id.gridImage)
        var name=row_view?.findViewById<TextView>(R.id.gridName)
        var stand=row_view?.findViewById<TextView>(R.id.gridStand)
        var age=row_view?.findViewById<TextView>(R.id.gridAge)
        var season=row_view?.findViewById<TextView>(R.id.gridSeason)
        var close=row_view?.findViewById<ImageButton>(R.id.button_close_item)
        var detail=row_view?.findViewById<ImageButton>(R.id.button_details)

        close!!.setOnClickListener(View.OnClickListener { v -> (viewGroup as GridView).performItemClick(v, position, 0) })
        detail!!.setOnClickListener(View.OnClickListener { v -> (viewGroup as GridView).performItemClick(v, position, 1) })

        settings.setTextViewSettings(name!!, context as Activity)
        settings.setTextViewSettings(stand!!, context as Activity)
        settings.setTextViewSettings(age!!, context as Activity)
        settings.setTextViewSettings(season!!, context as Activity)
        FillControllers(name!!, stand!!, age!!, season!!,avatar!!, itemModel.documents[position] as QueryDocumentSnapshot)
        return row_view!!
    }

    override fun getItem(position: Int): Any {
        return itemModel.documents[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return itemModel.size()
    }

    private fun FillControllers(name: TextView,stand: TextView,age: TextView,season: TextView, image: ImageView, doc: QueryDocumentSnapshot){
        settings.getLocale(context as Activity)
        settings.getMode(context as Activity)
        name.text=doc.data["name"] as String
        stand.text=doc.data["stand"] as String
        age.text="Age: "+ (doc.data["age"] as String)
        season.text="Seasons: "+ (doc.data["season"] as String)
        loadPicture(doc.data["avatar"] as String, image, context as Activity)
    }

}



