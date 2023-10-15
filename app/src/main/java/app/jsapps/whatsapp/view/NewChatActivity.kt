package app.jsapps.whatsapp.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.jsapps.whatsapp.databinding.ActivityNewChatBinding
import app.jsapps.whatsapp.view.adapters.contacts.ContactsAdapter
import app.jsapps.whatsapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewChatBinding

    private val adapter = ContactsAdapter()

    private val userVM: UserViewModel by viewModels()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()
        lifecycleScope.launch {
            userVM.setAllConversations(auth.currentUser!!.email!!)
            userVM.contactList.collect{
                if(it.isNotEmpty()){
                    Log.w(TAG, "value: $it")
                    adapter.users = it
                    binding.tvNoMessages.visibility = View.INVISIBLE
                    adapter.notifyDataSetChanged()
                }else{
                    Log.w(TAG, "value: $it")
                }
            }

        }
    }

    private fun initListeners() {
        binding.apply {
            rvContactos.adapter = adapter
            rvContactos.layoutManager = LinearLayoutManager(this@NewChatActivity)
            ivBack.setOnClickListener {
                Intent(this@NewChatActivity, MainActivity::class.java).also {
                    startActivity(it)
                }
            }
            newContactFab.setOnClickListener {
                startActivity(Intent(this@NewChatActivity, AddNewContactActivity::class.java))
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}