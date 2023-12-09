package com.example.personalrecordv4.model


import java.sql.Timestamp

import java.util.Date

data class WeightLog(val UserId: String, val URL: String, val Weight: Double, val Created_at : Date = Date()){
    constructor() : this("", "", 0.0) // default constructor needed for firebase

}

