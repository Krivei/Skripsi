package com.example.personalrecordv4.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class SplitRepository {
    var _listSplit: MutableLiveData<MutableList<Split>> = MutableLiveData(mutableListOf())
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private var db = Firebase.firestore.collection("Split")


    fun getSplit(mutableList: MutableList<String>) {
        isLoading.postValue(true)
        Log.i("split", "Split: Sukses $mutableList")
        db.whereIn(FieldPath.documentId(), mutableList).get().addOnSuccessListener { documents ->
        val listSplit = mutableListOf<Split>()
        for (document in documents) {
            val split = document.toObject<Split>()
            listSplit.add(split)
        }
        _listSplit.postValue(listSplit)
            isLoading.postValue(false)
    }.addOnFailureListener { exception ->
        Log.w("WorkOutPlan", "Error getting documents: ", exception)
    }

    }

}