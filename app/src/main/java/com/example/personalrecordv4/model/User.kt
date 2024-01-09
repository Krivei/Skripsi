package com.example.personalrecordv4.model

data class User(var email:String="", var name: String="", var workoutPlanId: MutableList<String>,var reminder : String){
    constructor() : this("", "", mutableListOf(),"") // default constructor needed for firebase


}
