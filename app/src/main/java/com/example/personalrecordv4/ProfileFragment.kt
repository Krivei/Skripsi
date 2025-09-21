package com.example.personalrecordv4

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.personalrecordv4.databinding.FragmentProfileBinding
import com.example.personalrecordv4.model.User
import com.example.personalrecordv4.viewmodel.UserViewModel


class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val userViewModel : UserViewModel by activityViewModels()
    private lateinit var binding: FragmentProfileBinding
    private var recyclerView : RecyclerView? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel.userData.observe(this) {
            if (it == null) {
                Log.i("Test", "User: Gagal")
            } else {
//                notificationViewModel.getNotif()
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        userViewModel.userData.observe(viewLifecycleOwner, Observer<User>{
            if(it == null){
                startActivity(Intent(activity, AuthActivity::class.java))
                requireActivity().finish()
            }else{
                binding.tvusername.text = userViewModel.userData.value!!.name
                binding.tvusername2.text = userViewModel.userData.value!!.name
                binding.tvemail.text = userViewModel.userData.value!!.email
                binding.tvNotifTime.text = userViewModel.userData.value!!.reminder
            }
        })



        var reminderConfirmation = AlertDialog.Builder(requireActivity())
        reminderConfirmation.setTitle("Confirm Reminder Change")
        reminderConfirmation.setNegativeButton("No"){
            dialog, which ->
            dialog.dismiss()
        }
        binding.btn8.setOnClickListener {
            reminderConfirmation.setMessage("Change reminder to 09:00 ?")
            reminderConfirmation.setPositiveButton("Yes"){
                    dialog,which ->

                userViewModel.editReminder("09:00")
                dialog.dismiss()

            }
            reminderConfirmation.show()
        }

        binding.btn16.setOnClickListener {
            reminderConfirmation.setMessage("Change reminder to 15:00 ?")
            reminderConfirmation.setPositiveButton("Yes"){
                dialog,which ->
//                notificationViewModel.setNotif("16:00")
                userViewModel.editReminder("15:00")
                dialog.dismiss()

            }
            reminderConfirmation.show()

        }

        binding.btnSignout.setOnClickListener {
            userViewModel.signOut()
            startActivity(Intent(activity, AuthActivity::class.java))
            requireActivity().finish()
        }

        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            intent.putExtra(EditProfileActivity.EXTRA_NAMA, binding.tvusername.text.toString())
            startActivity(intent)
        }

    }


}