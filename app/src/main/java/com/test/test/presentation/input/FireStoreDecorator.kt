package com.test.test.presentation.input

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.InputStream
import kotlin.coroutines.CoroutineContext

/**
 *
 * Gestor de eventos crear y obtener imagen de la base de datos de firestore
 */
class FireStoreDecorator( val _msgs : MutableLiveData<String> )
{
    var tempPhotoUri: Uri? = null
    var jobVM: CoroutineContext? = null
    var job = Job()

    private val _img = MutableLiveData<ByteArray>()
    val img: LiveData<ByteArray>
        get() = _img

    val msgs: LiveData<String>
        get() = _msgs

    private val db = Firebase.firestore

    /**
     * Sube la foto en menor calidad para tener un base64 reducido y poder almacenarlo en
     * firestore
     */
    fun uploadPhoto(inputStream: InputStream, callback: (String) -> Unit)
    {
        job.cancel()
        job = Job()
        jobVM = CoroutineScope(Dispatchers.Main + job).launch {
            val map = compressPhoto(inputStream)
            db.collection("users").add(map)
                .addOnSuccessListener {
                    callback(it.id)
                }.addOnFailureListener {
                    showMsgFromException(it)
                }
        }
    }

    private suspend fun compressPhoto(inputStream: InputStream) = withContext(Dispatchers.IO) {
        val pos = ByteArrayOutputStream()
        BitmapFactory.decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 20, pos )
        hashMapOf(
            "photo" to Base64.encode(pos.toByteArray(), Base64.NO_WRAP) .toString(Charsets.UTF_8)
        )
    }

    fun fetchImage(photoName: String)
    {
        db.collection("users").document(photoName).get()
            .addOnSuccessListener {
                val b64 = it.data?.get("photo").toString()
                _img.postValue(Base64.decode(b64, Base64.NO_WRAP))
            }.addOnFailureListener {
                showMsgFromException(it)
            }
    }

    private fun showMsgFromException(ex: Throwable)
    {
        Log.e(this::class.java.name, ex.message.orEmpty(), ex)
        ex.message ?: return
        ex.message!!.takeIf { it.isNotBlank() }?.let { msg ->
            _msgs.postValue(msg)
        }
    }

    fun clearMsgs()
    {
        _msgs.value = null
    }

    fun clearImg()
    {
        _img.value = null
    }
}