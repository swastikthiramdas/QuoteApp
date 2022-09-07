package com.swastik.shayariapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.swastik.shayariapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var db : FirebaseFirestore
    private lateinit var sharedPref: SharedPreferences
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        sharedPref = getSharedPreferences("email",Context.MODE_PRIVATE)
        val editor = sharedPref.edit()


        binding.SigninLogin.setOnClickListener {

            startActivity(Intent(this,SignInActivity::class.java))

        }
        binding.loginBtn.setOnClickListener {
            if (check()) {

                val email = binding.emailLogin.text.toString()
                val password = binding.passwordLogin.text.toString()

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                            editor.clear()
                            editor.putString("email",email)
                            editor.apply()
                            startActivity(Intent(this,MainActivity::class.java))
                            finish()

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else{

                Toast.makeText(this, "Empty Email or Password", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun check(): Boolean {
        if (
            binding.emailLogin.text.toString().trim { it <= ' ' }.isNotEmpty()
            &&
            binding.passwordLogin.text.toString().trim { it <= ' ' }.isNotEmpty()
        ) {
            if (binding.emailLogin.text.toString().endsWith("@gmail.com")){

                return true
            }
        }
        return false
    }
}