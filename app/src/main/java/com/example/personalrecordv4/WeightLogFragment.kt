package com.example.personalrecordv4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.adapter.WeightLogAdapter
import com.example.personalrecordv4.viewmodel.UserViewModel
import com.example.personalrecordv4.viewmodel.WeightLogViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class WeightLogFragment : Fragment(R.layout.fragment_weight_log) {
    private val userViewModel : UserViewModel by activityViewModels()
    private var recyclerView : RecyclerView? = null
    private val weightLogViewModel : WeightLogViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel.userData.observe(this) {
            if (it == null) {
                Log.i("Test", "User: Gagal")
            } else {
                weightLogViewModel.getWeightLog()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weight_log, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var progressBar = view?.findViewById<ProgressBar>(R.id.weight_progressbar)
        weightLogViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it == null) {
                Log.i("Test", "Loading: Gagal")
            } else {
                if (it) {
                    progressBar?.visibility = View.VISIBLE
                } else {
                    progressBar?.visibility = View.GONE
                }
            }
        }

        weightLogViewModel.listWeightLog.observe(viewLifecycleOwner) {
            if (it == null) {
                Log.i("Test", "Weight Log: Gagal")

            } else {
                recyclerView.apply {
                    recyclerView = view?.findViewById(R.id.rvWeightLog)
                    recyclerView?.layoutManager = GridLayoutManager(activity,2)
                    recyclerView?.setHasFixedSize(true)

                    recyclerView?.adapter = WeightLogAdapter(it)
                }
            }
        }
        val addWeightButton = view.findViewById<FloatingActionButton>(R.id.btnAddWeightLog)
        addWeightButton.setOnClickListener {
            val intent = Intent(activity, AddWeightLogActivity::class.java)
            startActivity(intent)
        }

    }


}