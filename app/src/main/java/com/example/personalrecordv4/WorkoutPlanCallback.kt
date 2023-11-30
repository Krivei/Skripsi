package com.example.personalrecordv4

import android.media.tv.AdResponse

interface WorkoutPlanCallback {
    fun onResponse(response: WorkoutPlanResponse)
}