package com.example.personalrecordv4.model

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.personalrecordv4.AddWorkoutPlanActivity
import com.example.personalrecordv4.viewmodel.ExerciseViewModel
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class SplitRepository {
    var _listSplit: MutableLiveData<MutableList<Split>?> = MutableLiveData(mutableListOf())
    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var newaddedid: String = ""
    var _newSplitList : MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())
    var _templateList : MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())
    var _singleSplit : MutableLiveData<Split?> = MutableLiveData()

    private var db = Firebase.firestore.collection("Split")
    private var dbw = Firebase.firestore.collection("WorkoutPlan")

    fun getSplit(mutableList: MutableList<String>) {
        isLoading.postValue(true)
        _listSplit.postValue(mutableListOf())
        Log.i("splitmutable", "Split: Sukses $mutableList")
        if (mutableList.contains("")){
            Log.i("SplitId", "WorkoutPlan is Empty")
            return
        } else {
            db.whereIn(FieldPath.documentId(), mutableList).orderBy("name", Query.Direction.ASCENDING).addSnapshotListener { documents, e ->
                val listSplit: MutableList<Split> = mutableListOf()
                if (documents != null) {
                    for (document in documents) {
                        val split = document.toObject<Split>()
                        for (items in split.exerciseId){
                        }
                        listSplit.add(split)
                    }
                    _listSplit.postValue(listSplit)
                    isLoading.postValue(false)
                }

            }
        }

    }

    fun getSingleSplit(id: String){
        db.document(id).addSnapshotListener { document, error ->
            if (document!=null && document.exists()){
                 _singleSplit.postValue(document.toObject<Split>())
            } else {
                _singleSplit.postValue(null)
            }
        }
    }
    fun assignExercise(splitId: String, exerciseId: String){
        db.document(splitId).update("exerciseId", FieldValue.arrayUnion(exerciseId))
    }

    fun removeExercise(splitId: String, exerciseIdToRemove: String) {
        db.document(splitId).update("exerciseId", FieldValue.arrayRemove(exerciseIdToRemove))
    }

    fun removeSplit(splitIds : MutableList<String>){
        splitIds.forEach {
            db.document(it).delete()
        }
    }


    fun makeSplitTemplate(planId: String, splitNumber: Int, exampleNumber: Int, reps: Int, sets: Int){

            val newSplit = Split("Split ${splitNumber}", mutableListOf())
//            db.add(newSplit).addOnSuccessListener { docref ->
//                dbw.document(planId).update("splitId", FieldValue.arrayUnion(docref.id))
//                    .addOnSuccessListener {
                        var listBaru = mutableListOf<String>()
                        if (exampleNumber==1){
                            //fb
                            if (splitNumber==1){
                                listBaru = mutableListOf<String>("Dumbbell Incline Bench Press","Pull Up","Dumbbell Bicep Curl","Dumbbell Skullcrusher","Dumbbell Overhead Press","Squat")
                            } else if(splitNumber==2){
                                 listBaru = mutableListOf<String>("Push Up","Dumbbell Row","Dumbbell Bicep Curl","Dumbbell Skullcrusher","Deadlift")
                            }
                        } else if(exampleNumber==2){
                            //ul
                            if (splitNumber==1){
                                 listBaru = mutableListOf<String>("Dumbbell Incline Bench Press","Push Up","Pull Up","Dumbbell Bicep Curl","Dumbbell Skullcrusher","Dumbbell Overhead Press")
                            } else if(splitNumber==2){
                                 listBaru = mutableListOf<String>("Squat","Deadlift")
                            }
                        } else if(exampleNumber==3){
                            //ppl
                            if (splitNumber==1){
                                 listBaru = mutableListOf<String>("Push Up","Dumbbell Overhead Press","Dumbbell Skullcrusher","Dumbbell Incline Bench Press")
                            } else if(splitNumber==2){
                                 listBaru = mutableListOf<String>("Pull Up","Deadlift","Dumbbell Row","Dumbbell Bicep Curl")
                            } else if(splitNumber==3){
                                 listBaru = mutableListOf<String>("Squat","Deadlift")
                            }
                        }
                        listBaru.forEach { unit ->
                            Log.i("Exercises","Template List Length :${unit}")
                        }
                        _templateList.postValue(listBaru)
//                        ExerciseViewModel().assignTemplateExercise(listBaru,reps,sets,docref.id)
//                    }
//            }


    }

fun assignSplit(planId: String, splitNumber: Int) {
    val documentId = mutableListOf<String>()
    var successfulUpdates = 0

    for (i in 1..splitNumber) {
        Log.i("SplitRepository", "Loop $i")
        val newSplit = Split("Split ${i}", mutableListOf())
        db.add(newSplit).addOnSuccessListener { docref ->
            dbw.document(planId).update("splitId", FieldValue.arrayUnion(docref.id))
                .addOnSuccessListener {
                    Log.i("SplitRepository", "Assign Successful")
                    documentId.add(docref.id)
                    successfulUpdates++

                    // Call postValue only after all updates are successful
                    if (successfulUpdates == splitNumber) {
                        _newSplitList.postValue(documentId)
                    }
                }
        }
    }
}

    fun addSplit(split: Split) {
        isLoading.postValue(true)
        Log.i("SplitRepository", "Split: Sukses $split")
        db.add(split).addOnSuccessListener {
            newaddedid = it.id
            Log.i("idsplit", "Split: Sukses ${it.id}")
            isLoading.postValue(false)
        }.addOnFailureListener {
            isLoading.postValue(false)
        }
    }

}