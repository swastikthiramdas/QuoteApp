package com.swastik.shayariapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.swastik.shayariapp.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignInBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        sharedPref = getSharedPreferences("email", Context.MODE_PRIVATE)

        binding.signinBtn.setOnClickListener {
            if (check()) {

                var email = binding.emailSign.text.toString()
                var password = binding.passwordSign.text.toString()
                var name = binding.nameSignin.text.toString()
                var phone = binding.phoneSign.text.toString()

                val users = hashMapOf(
                    "name" to name,
                    "phone" to phone,
                    "email" to email
                )
                val collection = db.collection("users")
                val query = collection.whereEqualTo("email", email).get()
                    .addOnSuccessListener { task ->
                        if (task.isEmpty) {
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {

                                        val editor = sharedPref.edit()
                                        editor.putString("email", email)
                                        editor.apply()
                                        collection.document(email).set(users)
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()

                                    } else {

                                        Toast.makeText(
                                            this,
                                            "Make Sure Your Internet Connection is on",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        } else {
                            Toast.makeText(this, "User Already Registered", Toast.LENGTH_SHORT)
                                .show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }


            } else {

                Toast.makeText(this, "Check if something went wrong", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun check(): Boolean {
        if (
            binding.emailSign.text.toString().trim { it <= ' ' }.isNotEmpty()
            &&
            binding.passwordSign.text.toString().trim { it <= ' ' }.isNotEmpty()
            &&
            binding.nameSignin.text.toString().trim { it <= ' ' }.isNotEmpty()
            &&
            binding.phoneSign.text.toString().trim { it <= ' ' }.isNotEmpty()
        ) {
            if (binding.emailSign.text.toString().endsWith("@gmail.com")) {

                return true
            }

        }
        return false
    }
}