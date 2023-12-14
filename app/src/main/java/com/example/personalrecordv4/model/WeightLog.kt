package com.example.personalrecordv4.model




import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import java.util.Date

data class WeightLog(val userId: String, val url: String,val weight: Double,val created_at: Timestamp){
    constructor() : this("", "", 0.0, Timestamp.now()) // default constructor needed for firebase

}

