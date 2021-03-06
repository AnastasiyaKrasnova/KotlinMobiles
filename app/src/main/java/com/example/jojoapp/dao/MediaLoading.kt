package com.example.jojoapp.dao

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
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

public fun storePicture (imageView: ImageView, path: String): Task<Uri> {
    imageView.isDrawingCacheEnabled = true
    imageView.buildDrawingCache()

    val storageRef = storage.reference
    val mountainImagesRef = storageRef.child("images/"+path)
    val bitmap = (imageView.drawable as BitmapDrawable).bitmap
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val data = baos.toByteArray()

    var uploadTask = mountainImagesRef.putBytes(data)
    val urlTask = uploadTask.continueWithTask { task ->
        if (!task.isSuccessful) {
            task.exception?.let {
                throw it
            }
        }
        mountainImagesRef.downloadUrl
    }
    return urlTask
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

