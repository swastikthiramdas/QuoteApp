package com.swastik.shayariapp

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.swastik.shayariapp.Adapter.AllQuoteAdapter
import com.swastik.shayariapp.Model.QuoteModel
import com.swastik.shayariapp.databinding.ActivityAllQuoteBinding
import com.swastik.shayariapp.databinding.DialogAddQuoteBinding

class AllQuoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllQuoteBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllQuoteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedPref = getSharedPreferences("email", Context.MODE_PRIVATE)
        db = FirebaseFirestore.getInstance()

        val email = sharedPref.getString("email", "-1")


        val name = intent.getStringExtra("name")

        binding.CatName.text = name.toString()

        binding.backQuote.setOnClickListener {

            onBackPressed()

        }

        db.collection("all").whereEqualTo("cat", name)
            .addSnapshotListener { value, error ->
                if (value != null) {

                    val list = arrayListOf<QuoteModel>()
                    val data = value.toObjects(QuoteModel::class.java)

                    list.addAll(data)
                    binding.rcvCat.layoutManager = LinearLayoutManager(this)
                    binding.rcvCat.adapter = AllQuoteAdapter(this, list)


                }
            }

        binding.addQuote.setOnClickListener {
            if (email != "-1") {

                val addQuoteDialog = Dialog(this)
                val binding = DialogAddQuoteBinding.inflate(layoutInflater)
                addQuoteDialog.setContentView(binding.root)

                if (addQuoteDialog.window != null)
                    addQuoteDialog.window!!.setBackgroundDrawable(ColorDrawable(0))

                binding.addtextQuote.setOnClickListener {

                    if (binding.addQuote.text.toString().trim { it <= ' ' }.isNotEmpty()) {
                        val mname = binding.addQuote.text.toString()
                        val uid = db.collection("EnglishQuotes").document().id
                        val data = QuoteModel(id = uid, data = mname, email = email, cat = name)

                        db.collection("all").document(uid).set(data).addOnCompleteListener {

                            if (it.isSuccessful) {
                                Toast.makeText(this, "Successfully Added", Toast.LENGTH_SHORT)
                                    .show()
                                addQuoteDialog.dismiss()
                            } else
                                Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT)
                                    .show()

                        }
                    } else {

                        Toast.makeText(this, "Empty Text", Toast.LENGTH_SHORT).show()

                    }
                }

                addQuoteDialog.show()

            } else {

                Toast.makeText(this, "Please Login To Add Quotes", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))

            }
        }
    }
}