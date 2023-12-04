package com.example.personalrecordv4.model

class Split (val name: String,val ExerciseId: MutableList<String>){
    constructor() : this("",mutableListOf()) // default constructor needed for firebase

}
