package com.example.personalrecordv4.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.personalrecordv4.viewmodel.UserViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import androidx.activity.viewModels
import com.example.personalrecordv4.WorkoutPlanCallback
import com.example.personalrecordv4.WorkoutPlanResponse
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.toObject

class WorkoutPlanRepository() {
    var _listWorkoutPlan: MutableLiveData<MutableList<WorkoutPlan>> = MutableLiveData(mutableListOf())
    var _listIdWorkoutPlan: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())
    var _spinnerAdapter: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())

    private val id = Firebase.auth.currentUser!!.uid
    private val db = Firebase.firestore.collection("user").document(id)
    private val dbwp = Firebase.firestore.collection("WorkoutPlan")
    val listWorkoutPlan: MutableList<WorkoutPlan> = mutableListOf()
    val spinnerAdapter: MutableList<String> = mutableListOf()
    init {
        updateData()
    }

    fun updateData(){
//        var tmp : MutableList<String> = mutableListOf()
//          db.addSnapshotListener{snapshot, e->
//              if (e!=null){
//                  Log.w("Error", e)
//              }
//              if (snapshot!=null){
//                  val workoutPlanList = snapshot.data?.get("WorkoutplanId") as Map <*, *>
//                  val listIdWorkoutPlan : MutableList<String> = mutableListOf()
//                  for (entry in workoutPlanList.entries){
//                      listIdWorkoutPlan.add(entry.value.toString())
//                      tmp.add(entry.value.toString())
//                  }
//                  _listIdWorkoutPlan.value= listIdWorkoutPlan
//
//              }
//
//          }
//        tmp.forEach{element ->
//            dbwp.document(element).addSnapshotListener{snapshot, e ->
//
//                if (snapshot != null) {
//                    val converted = snapshot.toObject<WorkoutPlan>()
//                    if (converted != null) {
//                        listWorkoutPlan.add(converted)
//                        spinnerAdapter.add(converted.name)
//                    }
////                    snapshot.toObject<WorkoutPlan>()?.let { listWorkoutPlan.add(it) }
//                }
//
//
//            }
//
//        }
//        _listWorkoutPlan.value = listWorkoutPlan
//        _spinnerAdapter.value=spinnerAdapter


//        db.addSnapshotListener{snapshot, e->
//            if (e!=null){
//                Log.w("TEST",e)
//                return@addSnapshotListener
//            }
//            if (snapshot != null){
//                val workoutPlanData = snapshot.data?.get("WorkoutplanId") as? Map<String, String>
//                val listWorkoutPlan: MutableList<WorkoutPlan> = mutableListOf()
//                val listIdWorkoutPlan: MutableList<String> = mutableListOf()
//                val spinnerAdapter: MutableList<String> = mutableListOf()
//                if (workoutPlanData != null) {
//                    var workoutPlans = mutableListOf<WorkoutPlan>()
//                    for (entry in workoutPlanData.entries){
//                         dbwp.document(entry.value.toString()).get().addOnSuccessListener {
//                             val doctype = snapshot.data?.get("Type") as String
//                             val docname = snapshot.data?.get("Name") as String
//                             var workoutPlan = WorkoutPlan(doctype, docname)
//                             val docsplits = snapshot.data?.get("SplitId") as Map<String, String>
//                             Log.i("WORKOUT",docname)
//                             Log.i("WORKOUT",doctype)
//                             Log.i("WORKOUT",docsplits.toString())
//                             for (entry in docsplits.entries){
//                                 workoutPlan.addSplitId(entry.key,entry.value)
//                             }
//                             listWorkoutPlan.add(workoutPlan)
//                             spinnerAdapter.add(workoutPlan.name)
//                         }
//
////                        val workoutPlan = WorkoutPlan(key.toString(), workoutPlanData[key].toString())
//
//                    }
//
//
//                    _listWorkoutPlan.value = listWorkoutPlan
//                    _spinnerAdapter.value = spinnerAdapter
//                }
//            }
//        }
    }
    fun get(callback: WorkoutPlanCallback){
//        db.get().addOnSuccessListener {
//            val response = WorkoutPlanResponse()
//            for ()
//        }
        db.addSnapshotListener{snapshot, e->
            if (e!=null){
                Log.w("TEST",e)
                return@addSnapshotListener
            }
            if (snapshot != null){
                val workoutPlanData = snapshot.data?.get("WorkoutplanId") as? Map<*, *>
                val response = WorkoutPlanResponse()
                val listWorkoutPlan: MutableList<WorkoutPlan> = mutableListOf()
                val listIdWorkoutPlan: MutableList<String> = mutableListOf()
                val spinnerAdapter: MutableList<String> = mutableListOf()

                if (workoutPlanData != null) {
                    for (entry in workoutPlanData){
                        var tmp = WorkoutPlan(entry.key.toString(), entry.value.toString())
                        response.workoutPlan.add(tmp)
                    }

                    _listWorkoutPlan.value = listWorkoutPlan
                    _spinnerAdapter.value = spinnerAdapter
                }
            }
        }

    }

    fun addPlan(){}
}
