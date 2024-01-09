package com.example.personalrecordv4

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.personalrecordv4.databinding.ActivityMainBinding
import com.example.personalrecordv4.viewmodel.UserViewModel
import com.pushwoosh.Pushwoosh
import com.pushwoosh.tags.Tags
import com.pushwoosh.tags.TagsBundle


class MainActivity : AppCompatActivity() {

    private val userViewModel : UserViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private var title : String = "Workout Plans"
    private val workoutListFragment = WorkoutListFragment()
    private val profileFragment = ProfileFragment()
    private val tutorialsFragment = TutorialsFragment()
    private val historyFragment = HistoryFragment()
    private val weightLogFragment = WeightLogFragment()
    private val tutorialPickFragment = TutorialPickFragment()
    val ONESIGNAL_APP_ID = "b7b52934-02b7-4c95-9fe7-7d3cfd1c04ac"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userViewModel.user.observe(this) { user ->
            if (user == null) {
                startActivity(Intent(this@MainActivity, AuthActivity::class.java))
                Pushwoosh.getInstance().unregisterForPushNotifications()
                finish()
            } else {
                Pushwoosh.getInstance().registerForPushNotifications()
            }
        }
        userViewModel.userData.observe(this){
            if (it!=null){
                Log.i("SHESH","SHESH")
                Pushwoosh.getInstance().sendTags(Tags.stringTag("Reminder",userViewModel.userData.value!!.reminder))
            }
        }
        binding.bottomnav.setOnItemSelectedListener {
            setMode(it.itemId)
            true
        }


    }

    fun setMode(mode: Int){
        when (mode){
            R.id.WorkoutNav -> {
                Log.i("Navigate", "Workout List")
                navigateToFragment(workoutListFragment)
                title = "Workout Plans"
            }
            R.id.ProfileNav -> {
                Log.i("Navigate", "Profile")
                navigateToFragment(profileFragment)
                title="Profile"
            }
            R.id.HistoryNav -> {
                Log.i("Navigate", "History")
                navigateToFragment(historyFragment)
                title="Workout History"
            }
            R.id.TutorialNav -> {
                Log.i("Navigate", "Tutorial")
                navigateToFragment(tutorialPickFragment)
                title="Tutorials"
            }
            R.id.WeightNav -> {
                Log.i("Navigate", "WeightLog")
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