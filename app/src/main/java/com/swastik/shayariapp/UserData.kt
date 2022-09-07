package com.swastik.shayariapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.swastik.shayariapp.Adapter.YourQuoteAdapter
import com.swastik.shayariapp.Model.QuoteModel
import com.swastik.shayariapp.databinding.ActivityAdminBinding

class UserData : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("email", Context.MODE_PRIVATE)

        val email = sharedPref.getString("email", "-1")
        db = FirebaseFirestore.getInstance()


        db.collection("all").whereEqualTo("email", email)
             .addSnapshotListener { value, error ->
                if (value != null) {

                    val list = arrayListOf<QuoteModel>()
                    val data = value.toObjects(QuoteModel::class.java)

                    list.addAll(data)

                    binding.rcvYourQuotes.layoutManager = LinearLayoutManager(this)
                    binding.rcvYourQuotes.adapter = YourQuoteAdapter(this, list)


                }
            }


    }
}