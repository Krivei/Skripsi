package com.example.personalrecordv4

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.adapter.ExerciseAdapter
import com.example.personalrecordv4.adapter.SplitAdapter
import com.example.personalrecordv4.model.Exercise
import com.example.personalrecordv4.model.Split
import com.example.personalrecordv4.viewmodel.ExerciseViewModel


class ExerciseFragment : Fragment(R.layout.fragment_exercise) {
    private var recyclerView: RecyclerView? = null
    private val exerciseViewModel: ExerciseViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exercise, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val data=arguments
        Log.i("Test", "Exercise ${requireArguments().getStringArray("exerciseId")}")
        if (data!=null){
            requireArguments().getStringArray("exerciseId")?.toMutableList()?.let {
                if (it!=null){
                    exerciseViewModel.getExercise(it)
                } else {
                    Log.i("Test", "Exercise: Gagal")
                }
            }
        }
        val progressBar = view?.findViewById<ProgressBar>(R.id.progress_loader)
        exerciseViewModel.isLoadingData.observe(viewLifecycleOwner, Observer{
            if (it==null){
                Log.i("Test", "Load Failed")
            } else {
                if (it){
                    progressBar?.visibility = View.VISIBLE
                } else {
                    progressBar?.visibility
                }
            }
        })
        exerciseViewModel.listExercise.observe(viewLifecycleOwner, Observer<MutableList<Exercise>?>{
            if (it==null){
                Log.i("Test","Split: Gagal")
            } else {
                recyclerView.apply {
                    recyclerView = view?.findViewById(R.id.rvExercise)
                    recyclerView?.layoutManager = LinearLayoutManager(activity)
                    recyclerView?.setHasFixedSize(true)
                    recyclerView?.adapter = ExerciseAdapter(it)
                }
            }
        })
    }


}