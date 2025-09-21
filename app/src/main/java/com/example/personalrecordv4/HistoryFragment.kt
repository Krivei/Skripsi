package com.example.personalrecordv4

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.adapter.HistoryAdapter
import com.example.personalrecordv4.listener.onItemClickListener
import com.example.personalrecordv4.viewmodel.HistoryViewModel
import com.example.personalrecordv4.viewmodel.UserViewModel


class HistoryFragment : Fragment() , onItemClickListener {
    private val historyViewModel : HistoryViewModel by activityViewModels()
    private var recyclerView : RecyclerView? = null
    private val userViewModel : UserViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel.userData.observe(this){
            if (it!=null){
                historyViewModel.getHistory()
            }
        }
    }

    override fun OnItemSelect(name: String, reps: Int, sets: Int, spltids: Array<String>){
        super.OnItemSelect(name, reps, sets, spltids)
        val intent = Intent(activity, HistoryDetailActivity::class.java)
        intent.putExtra("historyName",name)
        intent.putExtra("historyIds",spltids)
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        historyViewModel.listHistory.observe(viewLifecycleOwner){
            if (it!=null){
                recyclerView.apply {
                    recyclerView = view?.findViewById(R.id.rvHistory)
                    recyclerView?.layoutManager = LinearLayoutManager(activity)
                    recyclerView?.setHasFixedSize(true)
                    recyclerView?.adapter = HistoryAdapter(it,this@HistoryFragment)
                }
            }
        }
    }

}