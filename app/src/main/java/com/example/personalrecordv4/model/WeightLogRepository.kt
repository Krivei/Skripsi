package com.example.personalrecordv4.model

import android.content.Context
import android.graphics.Bitmap
import android.hardware.camera2.CameraManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.Date
import java.util.UUID

class WeightLogRepository() {
    var _listWeightLog : MutableLiveData<MutableList<WeightLog>> = MutableLiveData(mutableListOf())
    var _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var _WeightLog: MutableLiveData<WeightLog> = MutableLiveData()
    private val db = Firebase.firestore.collection("WeightLog")
    private val uid = Firebase.auth.currentUser!!.uid

    fun getWeightLog(){
        db.whereEqualTo("userId",uid).orderBy("created_at", Query.Direction.DESCENDING).get().addOnSuccessListener{ documents ->

            val listWeightLog = mutableListOf<WeightLog>()

            for (document in documents){
                val weightLog = document.toObject<WeightLog>()
                listWeightLog.add(weightLog)
            }
            _listWeightLog.postValue(listWeightLog)
            _isLoading.postValue(false)
        }.addOnFailureListener { exception ->
            Log.w("WeightLog", "Error getting documents: ", exception)
        }
    }

    fun addWeightLog(bitmap: Bitmap, berat: Double){
        val filename = "${UUID.randomUUID()}.jpg"

        val storageRef = FirebaseStorage.getInstance().reference.child("images/$filename")

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val timestamp = Timestamp.now()
        storageRef.putBytes(byteArray)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnCompleteListener {task ->
                    if (task.isSuccessful){
                        val downloadUrl = task.result.toString()
                        val weightLog = WeightLog(uid,downloadUrl,berat,timestamp)
                        db.document().set(weightLog)
                        Log.i("WeightLogRepository", "Save Success")
                    } else {
                        Log.i("WeightLogRepository", "Failed to save")
                    }
                }.addOnFailureListener {
                    Log.i("WeightLogRepository", "Failed to upload picture")
                }
            }
    }

    fun getDetails(url: String){
        db.whereEqualTo("url",url).get().addOnSuccessListener {documents ->
            if (documents.isEmpty()){
                Log.i("WeightLogRepository","Document is Empty")
            } else {
                val x = documents.first()
                val detail = x.toObject<WeightLog>()
                _WeightLog.postValue(detail)
                _isLoading.postValue(false)
            }
        }
    }




}