package com.example.personalrecordv4.model

import android.content.Context
import android.hardware.camera2.CameraManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.toObject
class WeightLogRepository() {
    var _listWeightLog : MutableLiveData<MutableList<WeightLog>> = MutableLiveData(mutableListOf())
    var _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    private val db = Firebase.firestore.collection("WeightLog")
    private val uid = Firebase.auth.currentUser!!.uid

    fun getWeightLog(){
        db.whereEqualTo("UserId",uid).orderBy("Created_at", Query.Direction.ASCENDING).get().addOnSuccessListener{ documents ->

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


}