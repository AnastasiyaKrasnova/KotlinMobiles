package com.example.jojoapp.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.AdapterView.inflate
import androidx.appcompat.app.AppCompatActivity
import com.example.jojoapp.R
import com.example.jojoapp.helpers.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class SettingsActivity : AppCompatActivity() {
    lateinit var language_spinner: Spinner
    lateinit var fontsize_spinner: Spinner
    lateinit var fonttype_spinner: Spinner
    private lateinit var bottomNav: BottomNavigationView
    private var settings: Settings=Settings()


    override fun onStart() {
        super.onStart()
        settings.getLocale(this)
        settings.setTextViewSettings(findViewById<TextView>(R.id.SettingsTitle),this)
        settings.setSwitchSettings(findViewById<Switch>(R.id.switch1),this)
        settings.getMode(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settingsview)
        val btn = findViewById<Switch>(R.id.switch1)
        bottomNav=findViewById(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        var language_names=arrayListOf(getString(R.string.settings_language),
            getString(R.string.settings_english),getString(R.string.settings_russian));
        var language_values=arrayListOf("","en","ru");

        var size_names=arrayListOf(getString(R.string.settings_size),"15","16", "17","18", "19","20", "21","22", "23","24", "25");
        var size_values=arrayListOf("","15","16", "17","18", "19","20", "21","22", "23","24", "25");

        var type_names=arrayListOf(getString(R.string.settings_type),"Arial" ,"Helvetica", "Disney");
        var type_values=arrayListOf("",R.font.arial.toString(),R.font.helvetica.toString(), R.font.disney.toString());

        btn.setOnCheckedChangeListener { _, isChecked ->
            if (btn.isChecked) {
                btn.text = getString(R.string.settings_darkmode_on)
                settings.setMode("MODE_NIGHT_YES",this)

            } else {
                btn.text = getString(R.string.settings_darkmode_off)
                settings.setMode("MODE_NIGHT_NO", this)
            }
        }

        var lang_model=settings.createSpinnerModelList(language_names, language_values)
        language_spinner = findViewById(R.id.language_spinner);
        language_spinner.adapter = CustomDropDownAdapter(this,lang_model)
        language_spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position>0){
                    selectLocale(lang_model[position].value!!)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}

        }

        var size_model=settings.createSpinnerModelList(size_names, size_values)
        fontsize_spinner = findViewById(R.id.fontsize_spinner)
        fontsize_spinner.adapter = CustomDropDownAdapter(this,size_model)
        fontsize_spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
               if (position>0){
                   selectSize(size_model[position].value!!.toInt()+3)
               }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        var type_model=settings.createSpinnerModelList(type_names, type_values)
        fonttype_spinner = findViewById(R.id.fonttype_spinner)
        fonttype_spinner.adapter = CustomDropDownAdapter(this,type_model)
        fonttype_spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position>0){
                    selectType(type_model[position].value!!.toInt())
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }


    }
    private fun selectLocale(localeName: String) {
        settings.setLocale(localeName, this)
        val refresh = Intent(
                this,
                SettingsActivity::class.java
        )
        startActivity(refresh)
    }

    private fun selectSize(size: Int) {
        settings.setFontSize(size.toFloat(), this)
        val refresh = Intent(
                this,
                SettingsActivity::class.java
        )
        startActivity(refresh)
    }

    private fun selectType(type: Int) {
        settings.setFontType(type, this)
        val refresh = Intent(
                this,
                SettingsActivity::class.java
        )
        startActivity(refresh)
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
}

class CustomDropDownAdapter(val context: Context, var dataSource: List<Spinner_Model>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var settings: Settings=Settings()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView
        if (view == null) {
            view= inflater.inflate(R.layout.spinner_layout,null)
        }
        val text = view!!.findViewById<TextView>(R.id.spinner_list_items) as TextView
        settings.setTextViewSettings(text, context as Activity)
        text.setText(dataSource[position].name!!);
        return view
    }

    override fun getItem(position: Int): Any? {
        return dataSource[position];
    }

    override fun getCount(): Int {
        return dataSource.size;
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }
}
