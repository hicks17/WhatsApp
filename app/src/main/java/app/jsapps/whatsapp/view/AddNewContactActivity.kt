package app.jsapps.whatsapp.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import app.jsapps.whatsapp.databinding.ActivityAddNewContactBinding
import app.jsapps.whatsapp.utils.toast
import app.jsapps.whatsapp.viewmodel.UserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNewContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewContactBinding
    private val userVM:UserViewModel by viewModels()
    private val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
    }

    private fun initListeners() {
        binding.apply {
            addContact.setOnClickListener {

                val correo = etEmail.text.toString()
                val nombre = etNombre.text.toString()

                Log.w(TAG, "nombre: $nombre, correo: $correo")

                if(correo.isNotEmpty() || nombre.isNotEmpty()) {

                    addNewContact(correo, nombre)
                    startActivity(Intent(this@AddNewContactActivity, NewChatActivity::class.java))
                    finish()
                }else{
                    toast("Agrega todos los datos")
                }
            }
        }
    }

    private fun addNewContact(email: String, nombre: String) {
        userVM.addNewContact(auth.currentUser!!.email!!, email, nombre)
    }
}