package com.example.personalrecordv4.model

data class Notifications(val days: MutableList<Int>, val time: String, val token: String, val userId: String) {

    constructor() : this(mutableListOf(),"","","")

}