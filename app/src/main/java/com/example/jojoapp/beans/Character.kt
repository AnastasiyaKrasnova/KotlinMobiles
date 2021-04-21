package com.example.jojoapp.beans

import android.os.Parcelable
import java.io.Serializable

class Character: Serializable{
    public var  name: String? =null
    public var stand: String? =null
    public var  age: String?=null
    public var season: String?=null
    public var avatar: String?=null
    public var description: String?=null
    public var images: ArrayList<String>?=null
    public var videos: ArrayList<String>?=null
    public var latitude: String? =null
    public var longitude: String? =null

    constructor(_name: String?,_stand: String?, _age: String?,
                _season: String?, _avatar: String?,_description: String?, _photo: ArrayList<String>?,
                _video: ArrayList<String>?, _latitude: String?, _longitude: String?){
        name=_name
        stand=_stand
        age=_age
        season=_season
        avatar=_avatar
        description=_description
        images=_photo
        videos=_video
        latitude=_latitude
        longitude=_longitude
    }

}
