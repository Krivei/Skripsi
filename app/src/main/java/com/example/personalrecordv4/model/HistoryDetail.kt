package com.example.personalrecordv4.model

import com.google.firebase.Timestamp

data class HistoryDetail (val name: String, val reps:MutableList<Int>, val weight: MutableList<Int>, val timestamp: Timestamp){
    constructor() : this("", mutableListOf(), mutableListOf(), Timestamp.now())
}