package com.example.personalrecordv4.model

 data class Basics (val name : String, val url: String, val descriptions: MutableList<String>) {
     constructor() : this("","", mutableListOf())
}