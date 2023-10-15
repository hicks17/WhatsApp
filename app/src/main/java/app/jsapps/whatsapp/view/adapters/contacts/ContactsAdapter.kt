package app.jsapps.whatsapp.view.adapters.contacts

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.jsapps.whatsapp.R
import app.jsapps.whatsapp.databinding.ConversationViewBinding
import app.jsapps.whatsapp.model.Usuario
import app.jsapps.whatsapp.view.ChatActivity

class ContactsAdapter: RecyclerView.Adapter<ContactsAdapter.ContactList>() {

    var users = listOf<Usuario>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactList {
        val view = LayoutInflater.from(parent.context)

            .inflate(R.layout.conversation_view, parent, false)

        return ContactList(view)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: ContactList, position: Int) {
        holder.putData(users[position])
    }

    inner class ContactList(val view: View) : RecyclerView.ViewHolder(view){

        private val binding = ConversationViewBinding.bind(view)
        @SuppressLint("SetTextI18n")
        fun putData(userData: Usuario){

            binding.apply {
                tvContact.text = userData.nombre
                tvLastMsg.text = userData.estado
                layout.setOnClickListener {
                    Intent(view.context, ChatActivity::class.java).also {
                        it.putExtra("email", userData.email)
                        it.putExtra("name", userData.nombre)
                        view.context.startActivity(it)
                    }
                }
            }

        }
    }
}