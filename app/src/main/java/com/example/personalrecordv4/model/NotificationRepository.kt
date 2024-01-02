package com.example.personalrecordv4.model

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class NotificationRepository {

    private val db = Firebase.firestore.collection("notification")
    private val uid =  Firebase.auth.currentUser!!.uid
    private val query = db.whereEqualTo("userId",uid)
    var _days : MutableLiveData<MutableList<Int>> = MutableLiveData(mutableListOf())
    var _notifications : MutableLiveData<Notifications> = MutableLiveData()
    val _token : MutableLiveData<String> = MutableLiveData()

    fun updateToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("UpdateToken", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            _token.postValue(task.result)
            // Log
            Log.d("NotificationRepository", task.result)
        })
        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful){
                val documentSnapshot = task.result!!.documents.firstOrNull()
                if (documentSnapshot!=null){
                    documentSnapshot.reference.update("token",_token.value)
                        .addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful){
                                Log.i("NotificationRepository", "Document Updated")
                            } else {
                                Log.i("NotificationRepository", "Update Failed")
                            }
                        }
                } else {
                    Log.i("NotificationRepository", "Document is empty")
                }
            } else {
                Log.i("NotificationRepository", "Task Failed")
            }
        }
    }

    fun setNotif(timestamp: String, days: MutableList<Int>){

    }

    fun getNotif(){
        query.addSnapshotListener { snapshot, e ->
            if (snapshot!=null){
                val x = snapshot.documents.firstOrNull()
                if (x != null) {
                    _notifications.postValue(x.toObject<Notifications>())
                    val tmp = x.toObject<Notifications>()
                    if (tmp != null) {
                        _days.postValue(tmp.days)
                    }
                    Log.i("NotificationRepository","Conversion Completed")
                }
            } else {
                Log.i("NotificationRepository","Null Snapshot")
            }
        }
    }
}