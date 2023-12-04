package com.example.personalrecordv4.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.personalrecordv4.viewmodel.WorkoutPlanViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects

import com.google.firebase.ktx.Firebase

class UserRepository(){
    var _user: MutableLiveData<FirebaseUser> = MutableLiveData()
    var _userData : MutableLiveData<User> = MutableLiveData()
    var _isRegistered: MutableLiveData<Boolean> = MutableLiveData()
    var _loginResult: MutableLiveData<Boolean> = MutableLiveData()
    var workoutViewModel = WorkoutPlanViewModel()
    private val db = Firebase.firestore.collection("user")
    private val auth = Firebase.auth
    init{
        if(auth.currentUser!=null){
            _user.value = auth.currentUser
            _loginResult.postValue(true)
            updateData()
        }
    }
    fun signUp(nama: String, email: String, password: String){
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
            task ->
            if (task.isSuccessful){
                val userId = auth.currentUser!!.uid
                val workout = mutableListOf<String>()
                workout.add("L8TIeoXh56xZZU8ryyWW")
                workout.add("LadzYV9WqbWBmRld0N64")
                workout.add("L5F66wExfEO6PObqis9W")
                val user = User(email,nama, workout)
                db.document(userId).set(user)
                _isRegistered.postValue(true)
            } else {
                _isRegistered.postValue(false)
                Log.i("Test", "Register: Gagal")
            }
        }
    }
    fun signIn(email: String, password: String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{task ->
            if (task.isSuccessful){
                _user.postValue(auth.currentUser)
                _loginResult.postValue(true)
                db.document(auth.currentUser!!.uid).get().addOnSuccessListener {document ->
                    _userData.postValue(document.toObject<User>())
                    val titleList = document.toObject<User>()!!.workoutPlanId
                    workoutViewModel.getWorkoutPlan(titleList)
                }
            }  else {
                _loginResult.postValue(false)
            }
            }.addOnFailureListener{
                _loginResult.postValue(false)
            }


    }
    fun signOut(){
        auth.signOut()
        Log.i("SignOut", "Sign Out Sukses")
    }
    fun updateData(){
        db.document(auth.currentUser!!.uid).addSnapshotListener{
            snapshot, e ->
            if (e != null) {
                Log.w("TEST", "Listen failed.", e)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                _userData.value = snapshot.toObject<User>()
            } else {
                Log.d("TEST", "Current data: null")
            }
        }
    }

 }
