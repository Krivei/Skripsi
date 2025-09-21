package com.example.personalrecordv4

import android.os.Bundle
import android.util.Log

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.adapter.HistoryDetailAdapter
import com.example.personalrecordv4.databinding.ActivityHistoryBinding
import com.example.personalrecordv4.viewmodel.HistoryViewModel


class HistoryDetailActivity : AppCompatActivity() {
    private val historyViewModel : HistoryViewModel by viewModels()
    private lateinit var binding : ActivityHistoryBinding
    private var recyclerView : RecyclerView? = null
    private var historyIds = mutableListOf<String>()
    private var historyName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        historyIds = intent.getStringArrayExtra("historyIds")!!.toMutableList()
        historyName = intent.getStringExtra("historyName").toString()
        binding.ivHistoryBack.setOnClickListener {
            finish()
        }
        binding.tvDetailTitle.text = historyName
        if (historyIds.isNotEmpty()){
            Log.i("WOI","Called")
            historyViewModel.getExerciseHistory(historyIds)
        }
        historyViewModel.listDetail.observe(this@HistoryDetailActivity){
            if (it!=null){
                recyclerView = findViewById(R.id.rvHistoryDetail)
                recyclerView?.layoutManager = LinearLayoutManager(this)
                recyclerView?.adapter = HistoryDetailAdapter(it)
            }
        }
    }

}