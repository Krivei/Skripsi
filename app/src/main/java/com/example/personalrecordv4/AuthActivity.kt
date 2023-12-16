package com.example.personalrecordv4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.personalrecordv4.viewmodel.UserViewModel
import com.example.personalrecordv4.databinding.ActivityAuthBinding


class AuthActivity : AppCompatActivity() {
    private val userViewModel : UserViewModel by viewModels()
    private lateinit var binding: ActivityAuthBinding
    private var title : String = "Sign In"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userViewModel.user.observe(this) { user ->
            if (user != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        val alert =  AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage("Mohon Cek Kembali Email dan Password Anda")
            .setPositiveButton("OK", null)
            .create()
        userViewModel.loginResult.observe(this) { result ->
            if (result == null){
                alert.dismiss()
            }
            if (result != null) {
               alert.show()
            }
        }

    }
}