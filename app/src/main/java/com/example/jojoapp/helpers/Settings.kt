package com.example.jojoapp.helpers

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.util.*


@Suppress("DEPRECATION")
class Settings: AppCompatActivity()  {

    public fun setTextViewSettings(text: TextView, activity: Activity){
        var settings= activity.getSharedPreferences("UserInfo",Context.MODE_PRIVATE)
        text.setTextSize(settings.getFloat("FontSize", 15.0F))
        Log.d("TAG", settings.getFloat("FontSize", 15.0F).toString())
        /*val face = Typeface.createFromAsset(assets,
                "fonts/epimodem.ttf")
        text.setTypeface(face)*/
    }

    /*public fun setSpinnerSettings(text: Spinner, activity: Activity){
        var settings= activity.getPreferences(Context.MODE_PRIVATE)
        text.
        text.setTextSize(settings.getFloat("FontSize", 10.0F))
        val face = Typeface.createFromAsset(assets,
                "fonts/epimodem.ttf")
        text.setTypeface(face)
    }*/

   public fun setLocale(localeName: String, activity: Activity) {
       var settings= activity.getPreferences(Context.MODE_PRIVATE)
       var editor = settings.edit()
       val currentLanguage=settings.getString("Language","en")
        if (localeName != currentLanguage) {
            val locale = Locale(localeName)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            editor.putString("Language", localeName)
            editor.commit()
            res.updateConfiguration(conf, dm)
        }
    }

    public fun setFontSize(size: Float, activity: Activity) {
        var settings= activity.getSharedPreferences("UserInfo",Context.MODE_PRIVATE)
        var editor = settings.edit()
        editor.putFloat("FontSize", size)
        editor.commit()
    }

    public fun setFontType(type: String, activity: Activity) {
        var settings= activity.getPreferences(Context.MODE_PRIVATE)
        var editor = settings.edit()
        editor.putString("FontType", type)
        editor.commit()
    }


    public fun setMode(mode: String,activity: Activity){
        var settings= activity.getPreferences(Context.MODE_PRIVATE)
        var editor = settings.edit()
        if (mode=="MODE_NIGHT_YES"){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            editor.putString("Mode", "MODE_NIGHT_YES")
            editor.commit()
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            editor.putString("Mode", "MODE_NIGHT_NO")
            editor.commit()
        }

    }

}
