package com.example.jojoapp.dao

import android.app.Activity
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.InputStream

@GlideModule
class AppGlide: AppGlideModule(){

    override fun registerComponents(
            context: android.content.Context,
            glide: Glide,
            registry: Registry
    ) {
        super.registerComponents(context, glide, registry)
        registry.append(
                StorageReference::class.java, InputStream::class.java,
                FirebaseImageLoader.Factory()
        )

    }
}

public var storage=Firebase.storage

public fun loadPicture(url: String, imageView: ImageView, activity: Activity) {
    val httpsReference = storage.getReferenceFromUrl(
            url)
    GlideApp.with(activity)
            .load(httpsReference)
            .into(imageView)
}

fun retrieveVideoFrameFromVideo(videoPath: String?): Bitmap? {
    var bitmap: Bitmap? = null
    var mediaMetadataRetriever: MediaMetadataRetriever? = null
    try {
        mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(videoPath, HashMap<String, String>())
        bitmap = mediaMetadataRetriever.frameAtTime
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        mediaMetadataRetriever?.release()
    }
    return bitmap
}

