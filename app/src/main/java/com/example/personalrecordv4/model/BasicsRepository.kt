package com.example.personalrecordv4.model

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class BasicsRepository {
    var _listBasics : MutableLiveData<MutableList<Basics>> = MutableLiveData(mutableListOf())
    var _basics : MutableLiveData<Basics> = MutableLiveData()
    private val db = Firebase.firestore.collection("Basics")

    fun getBasics(){
        db.orderBy("name", Query.Direction.DESCENDING).get().addOnSuccessListener { documents ->
            val listBasics = mutableListOf<Basics>()
            for (document in documents){
                val basics = document.toObject<Basics>()
                listBasics.add(basics)
            }
            _listBasics.postValue(listBasics)
        }
    }

    fun getBasicsDetails(name : String){
        db.whereEqualTo("name",name).get().addOnSuccessListener { documents ->
            if (documents.isEmpty){

            } else {
                val x = documents.first()
                val basics = x.toObject<Basics>()
                _basics.postValue(basics)
            }
        }
    }
}