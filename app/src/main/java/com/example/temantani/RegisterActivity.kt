package com.example.temantani

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.temantani.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference?.child("akunProfile")

        Registrasi()

        binding.apply {
            btnLoginDisini.setOnClickListener {
                Intent(this@RegisterActivity, LoginActivity::class.java).also {
                    startActivity(it)
                }
            }
        }

    }

    private fun Registrasi() {
        binding.apply {
            btnDaftar.setOnClickListener {
                //insialisasi form//
                val email = formEmail.text.toString().trim()
                val password = formPassword.text.toString().trim()
                //end//

                //Jika form tidak diisi maka error//
                if(TextUtils.isEmpty(formNama.text.toString())) {
                    formNama.setError("Nama harus diisi")
                    return@setOnClickListener
                }

                if (email.isEmpty()) {
                    formEmail.error = "Email harus diisi!"
                    formEmail.requestFocus()
                    return@setOnClickListener
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    formEmail.error = "Email tidak valid"
                    formEmail.requestFocus()
                    return@setOnClickListener
                }

                if (password.isEmpty() || password.length < 6) {
                    formPassword.error = "Password anda harus lebih dari 6 karakter"
                    formPassword.requestFocus()
                    return@setOnClickListener
                }

                auth.createUserWithEmailAndPassword(formEmail.text.toString(), formPassword.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val currentUser = auth.currentUser
                            val userDb = databaseReference?.child((currentUser?.uid!!))
                            userDb?.child("nama")?.setValue(formNama.text.toString())

                            Toast.makeText(this@RegisterActivity, "Registrasi sukses", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                            finish()

                        } else {

                            Toast.makeText(this@RegisterActivity, "Registrasi gagal, coba ulangi", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }

}