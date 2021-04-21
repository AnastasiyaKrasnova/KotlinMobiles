package com.example.jojoapp.activities

import android.app.Application
import android.os.Bundle
import com.yandex.mapkit.MapKitFactory

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("0063020e-0eea-45d6-baa6-4a60c9d9f9c6")
        MapKitFactory.initialize(this)
    }

}