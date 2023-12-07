package com.example.personalrecordv4.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.personalrecordv4.model.User
import com.example.personalrecordv4.model.UserRepository
import com.google.firebase.auth.FirebaseUser

class UserViewModel : ViewModel() {
    private var _user: MutableLiveData<FirebaseUser> = MutableLiveData()
    val user: LiveData<FirebaseUser>
        get() = _user

    private var _userData: MutableLiveData<User> = MutableLiveData()
    val userData: LiveData<User>
        get() = _userData

    private var _isRegistered: MutableLiveData<Boolean> = MutableLiveData()
    val isRegistered: LiveData<Boolean>
        get() = _isRegistered

    private var _loginResult: MutableLiveData<Boolean> = MutableLiveData()
    val loginResult: LiveData<Boolean>
        get() = _loginResult

    private val repo = UserRepository()

    init {
        _user = repo._user
        _userData = repo._userData
        _isRegistered = repo._isRegistered
        _loginResult = repo._loginResult
    }
    fun signUp(nama: String, email: String, password: String){
        repo.signUp(nama, email, password)
    }
    fun signIn(email: String, password: String){
        repo.signIn(email,password)
    }
    fun isNameValid(nama: String) : Boolean{
        return (nama.isNotBlank())
    }
    fun isEmailValid(email: String) : Boolean {
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches() && email.isNotBlank())
    }
    fun isPasswordValid(password: String) : Boolean {
        return password.length > 5
    }
    fun signOut(){
        repo.signOut()
    }

    fun editData(name: String, password: String){
        repo.editData(name,password)
    }
}