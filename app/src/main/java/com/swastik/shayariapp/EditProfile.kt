package com.swastik.shayariapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.swastik.shayariapp.databinding.ActivityEditProfileBinding

class EditProfile : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("email", Context.MODE_PRIVATE)
        db = FirebaseFirestore.getInstance()


        val email = sharedPref.getString("email", "-1")
        db.collection("users").document(email!!).get().addOnSuccessListener { document ->

            binding.nameProfile.setText(document.get("name").toString())
            binding.phoneProfile.setText(document.get("phone").toString())
            binding.emailProfile.setText(document.get("email").toString())

        }

        binding.saveBtn.setOnClickListener {
            if (check()) {

                val name = binding.nameProfile.text.toString()
                val phone = binding.phoneProfile.text.toString()

                db.collection("users").document(email).update("name", name,
                    "phone", phone,)
                Toast.makeText(this, "Updated Succsesfully", Toast.LENGTH_SHORT).show()
                onBackPressed()

            } else
                Toast.makeText(this, "Something is Empty", Toast.LENGTH_SHORT).show()
        }

    }

    private fun check(): Boolean {
        if (
            binding.nameProfile.text.toString().trim { it <= ' ' }.isNotEmpty()
            &&
            binding.phoneProfile.text.toString().trim { it <= ' ' }.isNotEmpty()
        ) {
            return true
        }
        return false

    }
}