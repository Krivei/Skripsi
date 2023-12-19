package com.example.personalrecordv4.model

class Split (var name: String, val exerciseId: MutableList<String>){
    constructor() : this("",mutableListOf()) // default constructor needed for firebase

}
