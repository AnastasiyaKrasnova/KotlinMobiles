package com.example.jojoapp.beans

import java.io.Serializable

class Character: Serializable{
    public var  doc_id: String? =null
    public var  name: String? =null
    public var stand: String? =null
    public var  age: String?=null
    public var season: String?=null
    public var avatar: String?=null
    public var description: String?=null
    public var photo: ArrayList<String>?=null
    public var video: ArrayList<String>?=null
    public var latitude: Double=0.0
    public var longitude: Double=0.0

    constructor(_doc_id: String?,_name: String?,_stand: String?, _age: String?,
                _season: String?, _avatar: String?,_description: String?, _photo: ArrayList<String>?,
                _video: ArrayList<String>?, _latitude: String?, _longitude: String?){
        doc_id=_doc_id
        name=_name
        stand=_stand
        age=_age
        season=_season
        avatar=_avatar
        description=_description
        photo=_photo
        video=_video
        latitude=_latitude!!.toDouble()
        longitude=_longitude!!.toDouble()
    }

}
