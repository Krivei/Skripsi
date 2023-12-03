package com.example.personalrecordv4.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects

import com.google.firebase.ktx.Firebase
//import java.sql.Timestamp
//import java.util.Date

class UserRepository(){
    var _user: MutableLiveData<FirebaseUser> = MutableLiveData()
    var _userData : MutableLiveData<User> = MutableLiveData()
    var _isRegistered: MutableLiveData<Boolean> = MutableLiveData()
    var _loginResult: MutableLiveData<Boolean> = MutableLiveData()
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
                val user = User(email,nama)
                user.addWorkoutPlanId("WorkoutplanId1", "L8TIeoXh56xZZU8ryyWW")
                user.addWorkoutPlanId("WorkoutplanId2","LadzYV9WqbWBmRld0N64")
                user.addWorkoutPlanId("WorkoutplanId3","L5F66wExfEO6PObqis9W")
                db.document(userId).set(user)
                _isRegistered.postValue(true)
            } else {
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

                Log.i("TEST", "Login Sukses", task.exception)

            }  else {
                _loginResult.postValue(false)
                Log.i("TEST", "Login Gagal", task.exception)
            }
            }.addOnFailureListener{
                Log.i("TEST", "Gagal Login")
            }


    }
    fun signOut(){
        auth.signOut()
        Log.i("TEST", "Sign Out Sukses")
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

    fun editData(nama: String, password: String){
        db.document(auth.currentUser!!.uid).update("name",nama)
        auth.currentUser!!.updatePassword(password)
    }
 }
