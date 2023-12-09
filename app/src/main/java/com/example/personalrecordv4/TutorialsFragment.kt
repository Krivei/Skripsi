package com.example.personalrecordv4

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.adapter.TutorialAdapter
import com.example.personalrecordv4.viewmodel.TutorialViewModel
import com.example.personalrecordv4.viewmodel.UserViewModel


class TutorialsFragment : Fragment(R.layout.fragment_tutorials) {
    private val userViewModel : UserViewModel by activityViewModels()
    private var recyclerView : RecyclerView? =null
    private val tutorialViewModel : TutorialViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel.userData.observe(this) {
            if (it == null) {
                Log.i("Test", "User: Gagal")
            } else {
                tutorialViewModel.getTutorial()
            }
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutorials, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var progressBar = view?.findViewById<ProgressBar>(R.id.weight_progressbar)
        tutorialViewModel.isLoading.observe(viewLifecycleOwner){
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

        tutorialViewModel.listTutorial.observe(viewLifecycleOwner){
            if (it == null){
                Log.i("Test", "Tutorial : Gagal")
            } else {
                recyclerView.apply {
                    recyclerView = view?.findViewById(R.id.rvTutorial)
                    recyclerView?.layoutManager = LinearLayoutManager(activity)
                    recyclerView?.setHasFixedSize(true)
                    recyclerView?.adapter = TutorialAdapter(it)
                }
            }
        }



    }



}