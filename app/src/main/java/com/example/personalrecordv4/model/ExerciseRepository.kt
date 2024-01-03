package com.example.personalrecordv4.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.personalrecordv4.viewmodel.SplitViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class ExerciseRepository {
    var _listExercise : MutableLiveData<MutableList<Exercise>?> = MutableLiveData(mutableListOf())
    var _listTutorial : MutableLiveData<MutableList<Exercise>> = MutableLiveData(mutableListOf())
    val _pickExercise : MutableLiveData<MutableList<Exercise>> = MutableLiveData(mutableListOf())
    var _listId : MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())
    val _tutorial: MutableLiveData<Exercise> = MutableLiveData()
    var _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var _isDone : MutableLiveData<Boolean> = MutableLiveData(false)
    var _loopNumber : MutableLiveData<Int> = MutableLiveData()
    private val db = Firebase.firestore.collection("Exercise")
    private val dbs = Firebase.firestore.collection("Split")
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
            Log.i("ExerciseRepository","Tutorial Fetched")
            _listTutorial.postValue(listTutorial)
            _isLoading.postValue(false)
        }.addOnFailureListener { exception ->
            Log.w("Tutorial", "Error getting documents: ", exception)
        }
    }

    fun checkExercise(rep: Int, set: Int){
        Log.i("AddWorkoutPlanActivity","Check Called")
        db.whereNotEqualTo("muscleType","").get().addOnSuccessListener {
            documents ->
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
            db.whereEqualTo("defaultSets",set).whereEqualTo("defaultReps",rep).get().addOnSuccessListener { documents ->
                if (documents.isEmpty){
                    Log.i("AddWorkoutPlanActivity","Document Empty Called")
                    val exerciseList = listTutorial
                    if (exerciseList != null) {
                        Log.i("exerciseList",exerciseList.size.toString())
                        exerciseList.forEach { exercise ->
                            Log.i("Looping","Looping")
                            var newExercise = exercise
                            newExercise.defaultReps = rep
                            newExercise.defaultSets = set
                            db.add(newExercise)
                        }
                    }
                } else {
                    Log.i("ExerciseRepository","Exercises Exist")
                }
            }
        }
    }
    fun getExerciseByNumbers(rep: Int, set: Int){
        db.whereEqualTo("defaultSets",set).whereEqualTo("defaultReps",rep).addSnapshotListener  { documents, e ->
            if (documents != null) {
                    var pickExercise = mutableListOf<Exercise>()
                    for (document in documents){
                        val exercise = document.toObject<Exercise>()
                        pickExercise.add(exercise)
                    }
                    _pickExercise.postValue(pickExercise)
            } else {
                Log.i("ExerciseRepository","Exercise Empty : $e")
            }
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
            db.whereIn(FieldPath.documentId(),mutableList).addSnapshotListener { documents, e ->
                val listExercise: MutableList<Exercise> = mutableListOf()
                if (documents != null) {
                    for (document in documents){
                        val exercise = document.toObject<Exercise>()
                        listExercise.add(exercise)
                    }
                }
                _listExercise.postValue(listExercise)
                _isLoading.postValue(false)
            }
        }
    }
    fun assignTemplateExercise(mutableList: MutableList<String>, rep : Int, set: Int, splitId: String,splitNumber: Int, loopNumber: Int) {
        val exerciselistid = mutableListOf<String>()
        Log.i("Exercises","${mutableList.size},${rep},${set},${splitId},${splitNumber},${loopNumber}")
        db.whereIn("name",mutableList).whereEqualTo("defaultReps",rep).whereEqualTo("defaultSets",set).get().addOnSuccessListener {documents ->
            if (documents!=null){
                for (document in documents) {
                    exerciselistid.add(document.id)
                    if (mutableList.size==exerciselistid.size){
                        dbs.document(splitId).update("exerciseId",FieldValue.arrayUnion(*exerciselistid.toTypedArray()))
                    }
                }
            }
            if (splitNumber!=loopNumber){
                _loopNumber.postValue(loopNumber+1)
            } else {
                _isDone.postValue(true)
            }
        }
    }

    fun pickExercise(nama: String, rep: Int, set: Int, splitId: String){
        db.whereEqualTo("name",nama).whereEqualTo("defaultReps",rep).whereEqualTo("defaultSets",set).get().addOnSuccessListener { documents ->
                if (!documents.isEmpty){
                    SplitViewModel().assignExercise(splitId,documents.first().id)
                }
        }
    }

    fun removeExercise(nama: String, rep: Int, set: Int, splitId: String){
        db.whereEqualTo("name",nama).whereEqualTo("defaultReps",rep).whereEqualTo("defaultSets",set).get().addOnSuccessListener { documents ->
            if (!documents.isEmpty){
                SplitViewModel().removeExercise(splitId,documents.first().id)
            }
        }
    }
}