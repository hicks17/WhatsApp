package app.jsapps.whatsapp.view.adapters.conversationAdapter

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.jsapps.whatsapp.R
import app.jsapps.whatsapp.databinding.MessageLayoutBinding
import app.jsapps.whatsapp.model.Message
import app.jsapps.whatsapp.view.adapters.conversationAdapter.ConversationAdapter.ConversationVH

class ConversationAdapter : RecyclerView.Adapter<ConversationVH>() {

    var conversation = mutableListOf<Message>()
    var mailAuthor = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationVH {
        val view = LayoutInflater.from(parent.context)

            .inflate(R.layout.message_layout, parent, false)

        return ConversationVH(view)
    }

    override fun onBindViewHolder(holder: ConversationVH, position: Int) {
        holder.putData(conversation[position])
    }

    override fun getItemCount() = conversation.size

    inner class ConversationVH(itemView:View) : RecyclerView.ViewHolder(itemView) {

        private val binding = MessageLayoutBinding.bind(itemView)

        fun putData(message: Message){

            binding.apply {
                if (message.author == mailAuthor){
                    Log.w(TAG, "truee")
                    tvMsg1.text = message.message
                    tvMsgTime1.text = message.datetime
                    linear1.visibility = View.VISIBLE
                    linear.visibility = View.INVISIBLE
                }else{
                    tvMsg.text = message.message
                    tvMsgTime.text = message.datetime
                    linear.visibility = View.VISIBLE
                    linear1.visibility = View.INVISIBLE
                    Log.w(TAG, "false")
                }

            }
        }
    }
}