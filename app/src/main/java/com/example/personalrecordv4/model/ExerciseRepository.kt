package com.example.personalrecordv4.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class ExerciseRepository {
    var _listExercise : MutableLiveData<MutableList<Exercise>?> = MutableLiveData(mutableListOf())
    var _listTutorial : MutableLiveData<MutableList<Exercise>> = MutableLiveData(mutableListOf())
    var _listId : MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())
    val _tutorial: MutableLiveData<Exercise> = MutableLiveData()
    var _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private val db = Firebase.firestore.collection("Exercise")
    private val uid = Firebase.auth.currentUser!!.uid

    fun getTutorial(){
        db.whereNotEqualTo("muscleType","").get().addOnSuccessListener {documents ->
            val listTutorial = mutableListOf<Exercise>()
            for (document in documents){
                val tutorial = document.toObject<Exercise>()
                var duplicate = false
                //check duplicate
                for (tmp in listTutorial){
                    if (tutorial.name==tmp.name){
                        duplicate = true
                        break
                    }
                }
                if (duplicate==false){
                    listTutorial.add(tutorial)
                }
            }
            _listTutorial.postValue(listTutorial)
            _isLoading.postValue(false)

        }.addOnFailureListener { exception ->
            Log.w("Tutorial", "Error getting documents: ", exception)
        }

    }
    fun getSingleTutorial( nama: String){
        Log.i("Get", "Single Tutorial")
        db.whereEqualTo("name",nama).limit(1).get().addOnSuccessListener {documents ->
            if (documents.isEmpty){
                Log.i("Get", "Document Not Found")
            } else {
                val x = documents.first()
                val stutorial = x.toObject<Exercise>()
                _tutorial.postValue(stutorial)

                _isLoading.postValue(false)
            }
        }
    }
    fun getExercise(mutableList: MutableList<String>){
        _isLoading.postValue(true)
        _listExercise.postValue(mutableListOf())
        Log.i("Exercise", "Exercise: Sukses $mutableList")
        if (mutableList.contains("")){
            Log.i("ExerciseId", "This split is Empty")
            return
        } else {
            db.whereIn(FieldPath.documentId(),mutableList).get().addOnSuccessListener { documents ->
                val listExercise: MutableList<Exercise> = mutableListOf()
                for (document in documents){
                    val exercise = document.toObject<Exercise>()
                    listExercise.add(exercise)
                }
                _listExercise.postValue(listExercise)
                _isLoading.postValue(false)
            }.addOnFailureListener { exception ->
                Log.w("Exercise", "Error getting exercisesss")
                _isLoading.postValue(false)
                _listExercise.postValue(null)
            }
        }
    }

    fun getExerciseIdFromName(mutableList: MutableList<String>) {
        var id:MutableList<String> = mutableListOf()
        db.whereIn("name",mutableList).get().addOnSuccessListener { documents ->
                for (document in documents) {
                    id.add(document.id)
                }
            _listId.postValue(id)
        }.addOnFailureListener { exception ->
                Log.w("Exercise", "Error getting documents: ", exception)
            }
    }
}