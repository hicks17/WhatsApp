package app.jsapps.whatsapp.view

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewOutlineProvider
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.jsapps.whatsapp.databinding.ActivityChatBinding
import app.jsapps.whatsapp.model.Message
import app.jsapps.whatsapp.view.adapters.conversationAdapter.ConversationAdapter
import app.jsapps.whatsapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {
    private lateinit var emailTalker:String
    private lateinit var userMail:String
    private lateinit var name:String
    private lateinit var ref:DatabaseReference

    private lateinit var binding: ActivityChatBinding
    private lateinit var auth: FirebaseAuth
    private var adapter = ConversationAdapter()
    private val userVM:UserViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val data = intent.extras
        auth = FirebaseAuth.getInstance()
        emailTalker = data!!.getString("email")!!
        name = data!!.getString("name")!!
        userMail = auth.currentUser!!.email!!
        val emailTalkerRef = emailTalker.replace('.', ',')
        val emailUserRef = userMail.replace('.', ',')
        ref = Firebase.database.getReference("users/$emailUserRef/chats/$emailTalkerRef")
        userVM.getAllConversations(userMail, emailTalker)
        adapter.mailAuthor = userMail
        initListeners()

        val conversationEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                        lifecycleScope.launch {
                            userVM.getAllConversations(userMail, emailTalker)
                            userVM.conversation.collect{
                                adapter.conversation = it.toMutableList()
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }


            override fun onCancelled(error: DatabaseError) {
                Log.d(ContentValues.TAG, "cancelado")
            }

        }

        ref.addValueEventListener(conversationEventListener)
    }

    private fun initListeners() {
        val decorView = window.decorView
        val windowBackground = decorView.background
        with(binding.toolbar) {
            setupWith(binding.root) // or RenderEffectBlur
                .setFrameClearDrawable(windowBackground) // Optional
                .setBlurRadius(5f)
                .setBlurAutoUpdate(true)

            outlineProvider = ViewOutlineProvider.BACKGROUND
            clipToOutline = true

        }
        binding.apply {

            conversationRV.layoutManager = LinearLayoutManager(this@ChatActivity)
            conversationRV.adapter = adapter
            ivBack.setOnClickListener {
                backToMain()
            }
            tvName.text = name
            ivSendMsg.setOnClickListener {
                if(etMessage.text.isNotEmpty()){
                    mandarMensaje()
                    etMessage.text.clear()
                }

            }
            lifecycleScope.launch {
                userVM.conversation.collect{
                    Log.w(TAG, "lista : $it")
                    adapter.conversation = it.toMutableList()
                }
            }
        }
    }

    private fun mandarMensaje() {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentDateTime= LocalDateTime.now()

        val time= currentDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)).removeSuffix(" AM").removeSuffix(" PM")
        val dateTime = formatter.format(Date())
        userVM.postMessage(
            emailTalker,
            Message(
                message = binding.etMessage.text.toString(),
                author = userMail,
                datetime = dateTime,
                timeStamp = Date().time.toInt()
            )
        )
    }


    private fun backToMain() {
        Intent(this, MainActivity::class.java).also { startActivity(it) }
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}