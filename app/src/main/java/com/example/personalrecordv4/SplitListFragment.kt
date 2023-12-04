package com.example.personalrecordv4

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.adapter.SplitAdapter
import com.example.personalrecordv4.adapter.WorkoutPlanAdapter
import com.example.personalrecordv4.model.Split
import com.example.personalrecordv4.model.WorkoutPlan
import com.example.personalrecordv4.viewmodel.SplitViewModel
import com.example.personalrecordv4.viewmodel.UserViewModel
import com.example.personalrecordv4.viewmodel.WorkoutPlanViewModel

class SplitListFragment : Fragment(R.layout.fragment_split_list) {
    private var recyclerView : RecyclerView? = null
    private val splitViewModel : SplitViewModel by activityViewModels()
    private val workoutPlanViewModel : WorkoutPlanViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workoutPlanViewModel.listWorkoutPlan.observe(this, Observer {
            if(it == null){
                Log.i("SplitFragment", "WorkoutPlan: Gagal")
            }else{
                splitViewModel.getSplit(it[0].SplitId.toMutableList())
                Log.i("SplitFragment", "WorkoutPlan: Sukses ${it[0].SplitId}")
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_split_list, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        var progressBar = view?.findViewById<ProgressBar>(R.id.progress_loader)
        splitViewModel.isLoadingData.observe(viewLifecycleOwner, Observer {
            if(it == null){
                Log.i("Test", "Loading: Gagal")
            }else{
                if(it){
                    progressBar?.visibility = View.VISIBLE
                }else{
                    progressBar?.visibility = View.GONE
                }
            }
        })
        splitViewModel.splitData.observe(viewLifecycleOwner, Observer<MutableList<Split>>{
            if(it == null){
                Log.i("Test", "WorkoutPlan: Gagal")
            }else{
                recyclerView.apply {
                    // set a LinearLayoutManager to handle Android
                    // RecyclerView behavior
                    recyclerView = view?.findViewById(R.id.splitRecyclerView)
                    recyclerView?.layoutManager = LinearLayoutManager(activity)
                    recyclerView?.setHasFixedSize(true)
                    // set the custom adapter to the RecyclerView
                    recyclerView?.adapter = SplitAdapter(it)
                }
            }
        })

    }

}