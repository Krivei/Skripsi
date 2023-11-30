package com.example.personalrecordv4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.personalrecordv4.databinding.ActivityMainBinding
import com.example.personalrecordv4.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private val userViewModel : UserViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private var title : String = "Workout Plans"
    private val workoutListFragment = WorkoutListFragment()
    private val profileFragment = ProfileFragment()
    private val tutorialsFragment = TutorialsFragment()
    private val historyFragment = HistoryFragment()
    private val weightLogFragment = WeightLogFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userViewModel.user.observe(this, Observer<FirebaseUser>{ user ->
            if(user == null){
                startActivity(Intent(this@MainActivity, AuthActivity::class.java))
                finish()
            }
        })
//        binding.button.setOnClickListener {
//            userViewModel.logOut()
//            startActivity(Intent(this@MainActivity, AuthActivity::class.java))
//            finish()
//        }
        binding.bottomnav.setOnItemSelectedListener {
            setMode(it.itemId)
            true
        }
    }

    fun setMode(mode: Int){
        when (mode){
            R.id.WorkoutNav -> {
                navigateToFragment(workoutListFragment)
                title = "Workout Plans"
            }
            R.id.ProfileNav -> {
                navigateToFragment(profileFragment)
                title="Profile"
            }
            R.id.HistoryNav -> {
                navigateToFragment(historyFragment)
                title="Workout History"
            }
            R.id.TutorialNav -> {
                navigateToFragment(tutorialsFragment)
                title="Tutorials"
            }
            R.id.WeightNav -> {
                navigateToFragment(weightLogFragment)
                title="Weight Logs"
            }
        }
        setActionBarTitle(title)
    }

    private fun navigateToFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, fragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }

}