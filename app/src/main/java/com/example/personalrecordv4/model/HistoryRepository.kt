package com.example.personalrecordv4.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class HistoryRepository {
    var _listHistory : MutableLiveData<MutableList<History>> = MutableLiveData(mutableListOf())
    var _listDetail : MutableLiveData<MutableList<HistoryDetail>> = MutableLiveData(mutableListOf())
    var _historyId : MutableLiveData<String> = MutableLiveData()
    var _historyDetail: MutableLiveData<String> = MutableLiveData()
    private val db = Firebase.firestore.collection("History")
    private val history = Firebase.firestore.collection("ExerciseHistory")
    private val uid = Firebase.auth.currentUser!!.uid
    fun getHistory(){
        db.whereEqualTo("userId",uid).orderBy("date", Query.Direction.DESCENDING).limit(12).addSnapshotListener{ documents, error ->
            val listHistory = mutableListOf<History>()
            if (documents!=null){
                for (document in documents){
                    val history = document.toObject<History>()
                    listHistory.add(history)
                    Log.i("cool",history.exerciseLogs.size.toString())
                }
                _listHistory.postValue(listHistory)
            }
        }
    }
    fun getExerciseHistory(id: MutableList<String>){
        for (i in id){
            Log.i("WOI","ID : $i")
        }
        history.whereIn(FieldPath.documentId(),id).orderBy("timestamp", Query.Direction.ASCENDING).addSnapshotListener { documents, error ->
            val listDetail = mutableListOf<HistoryDetail>()
            if (documents!=null){
                for (document in documents){
                    if (document!=null){
                        val detail = document.toObject<HistoryDetail>()
                        Log.i("WOI","getExerciseHistory: ${documents.size()}")
                        listDetail.add(detail)
                        Log.i("WOI","getExerciseHistory: ${document.id}")
                    }
                }
                _listDetail.postValue(listDetail)
            } else {
                Log.i("WOI","getExerciseHistory Failed")

            }
        }
    }
    //bikin history baru saat exercise pertama kali selesai
    fun createHistory(name: String,workoutType: String,split: String,firstExercise: String){
        var obj = History(name,uid,workoutType, mutableListOf(firstExercise), Timestamp.now(),split)
        db.add(obj).addOnSuccessListener {
            _historyId.postValue(it.id)
        }
    }

    fun updateHistory(id: String, update: String){
        db.document(id).update("exerciseLogs",FieldValue.arrayUnion(update))
    }

    // bikin history detail
    fun createHistoryDetail(name: String,reps: MutableList<Int>, weight: MutableList<Int>){
        var obj = HistoryDetail(name, reps, weight, Timestamp.now())
        history.add(obj).addOnSuccessListener {
            _historyDetail.postValue(it.id)
        }
    }

}