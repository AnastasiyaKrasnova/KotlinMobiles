package com.example.jojoapp.helpers

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.res.ResourcesCompat
import com.example.jojoapp.R
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class Settings: AppCompatActivity()  {

    public fun setTextViewSettings(text: TextView, activity: Activity){
        var settings= activity.getSharedPreferences("UserInfo",Context.MODE_PRIVATE)
        text.setTextSize(settings.getFloat("FontSize", 15.0F))
        val face = ResourcesCompat.getFont(activity, settings.getInt("FontType", R.font.arial))
        text.setTypeface(face)
    }

    public fun setButtonSettings(button: Button, activity: Activity){
        var settings= activity.getSharedPreferences("UserInfo",Context.MODE_PRIVATE)
        button.setTextSize(settings.getFloat("FontSize", 15.0F))
        val face = ResourcesCompat.getFont(activity, settings.getInt("FontType", R.font.arial))
        button.setTypeface(face)

    }

    public fun setTextEditSettings(text: EditText, activity: Activity){
        var settings= activity.getSharedPreferences("UserInfo",Context.MODE_PRIVATE)
        text.setTextSize(settings.getFloat("FontSize", 15.0F))
        val face = ResourcesCompat.getFont(activity, settings.getInt("FontType", R.font.arial))
        text.setTypeface(face)
    }

    public fun setSwitchSettings(text: Switch, activity: Activity){
        var settings= activity.getSharedPreferences("UserInfo",Context.MODE_PRIVATE)
        text.setTextSize(settings.getFloat("FontSize", 15.0F))
        val face = ResourcesCompat.getFont(activity, settings.getInt("FontType", R.font.arial))
        text.setTypeface(face)
    }

   public fun setLocale(localeName: String, activity: Activity) {
       var settings= activity.getSharedPreferences("UserInfo",Context.MODE_PRIVATE)
       var editor = settings.edit()
       val currentLanguage=settings.getString("Language","en")
        if (localeName != currentLanguage) {
            val locale = Locale(localeName)
            val res = activity.resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            editor.putString("Language", localeName)
            editor.commit()
            res.updateConfiguration(conf, dm)
        }
    }

    public fun getLocale(activity: Activity) {
        var settings= activity.getSharedPreferences("UserInfo",Context.MODE_PRIVATE)
        val currentLanguage=settings.getString("Language","en")
        Log.d("TAG", settings.getString("Language","en")!!)
        val locale = Locale(currentLanguage)
        val res = activity.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = locale
        res.updateConfiguration(conf, dm)
    }

    public fun setFontSize(size: Float, activity: Activity) {
        var settings= activity.getSharedPreferences("UserInfo",Context.MODE_PRIVATE)
        var editor = settings.edit()
        editor.putFloat("FontSize", size)
        editor.commit()
    }


    public fun setFontType(type_id: Int, activity: Activity) {
        var settings= activity.getSharedPreferences("UserInfo",Context.MODE_PRIVATE)
        var editor = settings.edit()
        editor.putInt("FontType", type_id)
        editor.commit()
    }


    public fun setMode(mode: String,activity: Activity){
        var settings= activity.getSharedPreferences("UserInfo",Context.MODE_PRIVATE)
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

    public fun getMode(activity: Activity){
        var settings= activity.getSharedPreferences("UserInfo",Context.MODE_PRIVATE)
        var mode = settings.getString("Mode","MODE_NIGHT_NO")
        if (mode=="MODE_NIGHT_YES"){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    public fun createSpinnerModelList(names: ArrayList<String>, values: ArrayList<String>): ArrayList<Spinner_Model>{
        var spinner_model_list= ArrayList<Spinner_Model>()
        for (i in 0..(names.count()-1)){
            spinner_model_list.add(Spinner_Model(names[i], values[i]))
        }
        return spinner_model_list;
    }

}

public class Spinner_Model{
    public var name: String?=null
    public var value: String?=null
    constructor(_name: String?, _value: String){
        name=_name;
        value=_value;
    }
}
