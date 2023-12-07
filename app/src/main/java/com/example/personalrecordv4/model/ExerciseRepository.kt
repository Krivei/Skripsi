package com.example.personalrecordv4.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class ExerciseRepository {
    var _listExercise : MutableLiveData<MutableList<Exercise>> = MutableLiveData(mutableListOf())
    var _listTutorial : MutableLiveData<MutableList<Exercise>> = MutableLiveData(mutableListOf())
    var _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    private val db = Firebase.firestore.collection("Exercise")
    private val uid = Firebase.auth.currentUser!!.uid

    fun getTutorial(){
        db.whereNotEqualTo("TutorialVid",null).get().addOnSuccessListener {documents ->
            val listTutorial = mutableListOf<Exercise>()

            for (document in documents){
                val tutorial = document.toObject<Exercise>()
                listTutorial.add(tutorial)
            }
            _listTutorial.postValue(listTutorial)
            _isLoading.postValue(false)

        }.addOnFailureListener { exception ->
            Log.w("Tutorial", "Error getting documents: ", exception)
        }
    }

//    fun getExercise(){
//        db.whereEqualTo().get()
//    }
}