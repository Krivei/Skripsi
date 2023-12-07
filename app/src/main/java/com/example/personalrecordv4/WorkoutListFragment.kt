package com.example.personalrecordv4

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.model.WorkoutPlan
import com.example.personalrecordv4.viewmodel.UserViewModel
import com.example.personalrecordv4.adapter.WorkoutPlanAdapter
import com.example.personalrecordv4.databinding.FragmentProfileBinding
import com.example.personalrecordv4.viewmodel.WorkoutPlanViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [WorkoutListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WorkoutListFragment : Fragment(R.layout.fragment_workout_list) {
    private val userViewModel : UserViewModel by activityViewModels()
    private var recyclerView : RecyclerView? = null
    private lateinit var btnAddWorkout : Button
    private val workoutViewModel : WorkoutPlanViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel.userData.observe(this, Observer {
            if(it == null){
                Log.i("Test", "User: Gagal")
            }else{
                workoutViewModel.getWorkoutPlan(it.workoutPlanId)
                Log.i("Test", "User: Sukses")
            }
        })

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workout_list, container, false)
        // Inflate the layout for this fragment
    }
    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
//        btnAddWorkout.findViewById<Button>(R.id.addWorkoutBtn)
//        btnAddWorkout.setOnClickListener {
////            val intent = Intent(activity, AddWorkoutActivity::class.java)
////            startActivity(intent)
//        }
        var progressBar = view?.findViewById<ProgressBar>(R.id.progress_loader)
        workoutViewModel.isLoadingData.observe(viewLifecycleOwner, Observer {
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
       workoutViewModel.listWorkoutPlan.observe(viewLifecycleOwner, Observer<MutableList<WorkoutPlan>>{
            if(it == null){
                Log.i("Test", "WorkoutPlan: Gagal")
            }else{
                recyclerView.apply {
                    // set a LinearLayoutManager to handle Android
                    // RecyclerView behavior
                    recyclerView = view?.findViewById(R.id.workoutRecyclerView)
                    recyclerView?.layoutManager = LinearLayoutManager(activity)
                    recyclerView?.setHasFixedSize(true)
                    // set the custom adapter to the RecyclerView
                    recyclerView?.adapter = WorkoutPlanAdapter(it)
                }
            }
        })
        }

}