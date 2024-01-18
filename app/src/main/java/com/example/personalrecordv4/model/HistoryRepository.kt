package com.example.personalrecordv4.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HistoryRepository {
    private val db = Firebase.firestore.collection("History")
    private val uid = Firebase.auth.currentUser!!.uid
    fun getHistory(){
        db.whereEqualTo("userId",uid).addSnapshotListener{ documents , error ->

        }
    }
}