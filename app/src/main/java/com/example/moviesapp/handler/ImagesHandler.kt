package com.example.moviesapp.handler

import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.moviesapp.factory.createGetHttpUrlConnection
import java.io.File
import java.net.HttpURLConnection
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.fileVisitor


const val IMAGE_API_URL = "https://image.tmdb.org/t/p/original"
@RequiresApi(Build.VERSION_CODES.O)
fun downloadImageAndStore(context: Context, url: String): String? {

    val url_path = IMAGE_API_URL + url
    val file: File = createLocaleFile(context, url.replace("/", ""))

    try{
        val con: HttpURLConnection = createGetHttpUrlConnection(url_path)
        Files.copy(con.inputStream, Paths.get(file.toURI()))
        return file.absolutePath
    }catch (e:Exception){
        Log.e("IMAGE HANDLER", e.toString(), e);
    }
    return null
}



fun createLocaleFile(context: Context, filename: String): File {
    val dir = context.applicationContext.getExternalFilesDir(null)
    val file = File(dir, filename)
    if(file.exists()){
        file.delete()
    }

    return file
}
