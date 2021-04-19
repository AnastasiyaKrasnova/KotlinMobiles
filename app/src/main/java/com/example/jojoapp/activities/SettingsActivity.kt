package com.example.jojoapp.activities
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.jojoapp.R
import com.example.jojoapp.helpers.Settings
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class SettingsActivity : AppCompatActivity() {
    lateinit var language_spinner: Spinner
    lateinit var fontsize_spinner: Spinner
    lateinit var fonttype_spinner: Spinner
    private lateinit var bottomNav: BottomNavigationView
    private var currentLanguage = "en"
    private var currentLang: String? = null
    private var settings: Settings=Settings()

    override fun onStart() {
        super.onStart()
        settings.setTextViewSettings(findViewById<TextView>(R.id.SettingsTitle),this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settingsview)
        val btn = findViewById<Switch>(R.id.switch1)
        bottomNav=findViewById(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        btn.setOnCheckedChangeListener { _, isChecked ->
            if (btn.isChecked) {
                btn.text = "Disable dark mode"
                settings.setMode("MODE_NIGHT_YES",this)

            } else {
                btn.text = "Enable dark mode"
                settings.setMode("MODE_NIGHT_NO", this)
            }
        }

        currentLanguage = intent.getStringExtra(currentLang).toString()
        language_spinner = findViewById(R.id.language_spinner)
        val language_list = ArrayList<String>()
        language_list.add("Select language")
        language_list.add(getString(R.string.settings_english))
        language_list.add(getString(R.string.settings_russian))
        val language_adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, language_list)
        language_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        language_spinner.adapter = language_adapter
        language_spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                    }
                    1-> selectLocale("en")
                    2-> selectLocale("ru")
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        fontsize_spinner = findViewById(R.id.fontsize_spinner)
        val fontsize_list = ArrayList<String>()
        fontsize_list.add("Select font size")
        for (i in 15..25){
            fontsize_list.add(i.toString())
        }
        val fontsize_adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, fontsize_list)
        fontsize_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fontsize_spinner.adapter = fontsize_adapter
        fontsize_spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
               if (position==0){
               }
               else if (position==1){
                    selectSize(position+24)
               }
               else  {
                   selectSize(position+14)
               }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        fonttype_spinner = findViewById(R.id.fonttype_spinner)
        val fonttype_list = ArrayList<String>()
        fonttype_list.add("Select font type")
        fontsize_list.add(getString(R.string.settings_english))
        fontsize_list.add(getString(R.string.settings_russian))
        val fonttype_adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, fonttype_list)
        fonttype_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fonttype_spinner.adapter = fonttype_adapter
        fonttype_spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                    }
                    1-> selectLocale("en")
                    2-> selectLocale("ru")
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
    private fun selectLocale(localeName: String) {
        settings.setLocale(localeName, this)
        val refresh = Intent(
                this,
                SettingsActivity::class.java
        )
        refresh.putExtra(currentLang, localeName)
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

    private fun selectType(type: String) {
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