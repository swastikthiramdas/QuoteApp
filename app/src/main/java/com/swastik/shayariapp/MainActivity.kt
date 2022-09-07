package com.swastik.shayariapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.swastik.shayariapp.Adapter.CatQuotesAdapter
import com.swastik.shayariapp.Model.CatModel
import com.swastik.shayariapp.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences("email", Context.MODE_PRIVATE)

        val email = sharedPref.getString("email", "-1")
        db = FirebaseFirestore.getInstance()

        GlobalScope.launch {
            db.collection("EnglishQuotes").addSnapshotListener { value, error ->
                if (value != null) {
                    val list = ArrayList<CatModel>()
                    val data = value.toObjects(CatModel::class.java)
                    list.addAll(data)

                    binding.rcvQuotes.layoutManager = LinearLayoutManager(this@MainActivity)
                    binding.rcvQuotes.adapter = CatQuotesAdapter(this@MainActivity, list)
                }
            }

        }


        val View = binding.NavigationView.getHeaderView(0)
        val name = View.findViewById<TextView>(R.id.name_header)
        val memail = View.findViewById<TextView>(R.id.email_header)
        val layout = View.findViewById<LinearLayout>(R.id.header_layout)


        if (email != "-1") {

            db.collection("users").document(email!!).get().addOnSuccessListener { document ->

                name.text = document.get("name").toString()
                memail.text = document.get("email").toString()
            }

        } else {

            name.setText("Hey User Login To Upload your Quote!!")
            memail.setText("CLICK ME TO LOGIN!!")
            layout.setOnClickListener {

                startActivity(Intent(this, LoginActivity::class.java))
            }

        }




        binding.NavigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.share -> {

                    try {
                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Quotes")
                        var shareMessage = "\nInstall this application for quotes\n\n"
                        shareMessage =
                            """
                           ${shareMessage}https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}       
                           """.trimIndent()
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                        startActivity(Intent.createChooser(shareIntent, "choose one"))
                    } catch (e: Exception) {
                        //e.toString();
                    }
                    true
                }
                R.id.Post -> {

                    if (email != "-1")
                        startActivity(Intent(this, UserData::class.java))
                    else
                        startActivity(Intent(this, LoginActivity::class.java))

                    true

                }
                R.id.logout -> {


                    val editor = sharedPref.edit()
                    editor.clear()
                    editor.apply()
                    startActivity(Intent(this, LoginActivity::class.java))

                    true
                }
                R.id.profile_edit -> {

                    if (email != "-1") {
                        startActivity(Intent(this, EditProfile::class.java))
                    } else {
                        startActivity(Intent(this, LoginActivity::class.java))
                        Toast.makeText(
                            this,
                            "You Must Login To Edit Your Profile",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    true
                }
                else -> false
            }
        }



        binding.btnMenu.setOnClickListener {
            if (binding.DrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                binding.DrawerLayout.closeDrawer(Gravity.LEFT)
            } else {
                binding.DrawerLayout.openDrawer(Gravity.LEFT)
            }
        }

    }

    override fun onBackPressed() {
        if (binding.DrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            binding.DrawerLayout.closeDrawer(Gravity.LEFT)
        } else {
            super.onBackPressed()
        }
    }
}