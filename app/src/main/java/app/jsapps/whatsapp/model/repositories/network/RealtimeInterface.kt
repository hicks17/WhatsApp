package app.jsapps.whatsapp.model.repositories.network

import android.content.BroadcastReceiver
import app.jsapps.whatsapp.model.Message
import app.jsapps.whatsapp.model.Usuario

interface RealtimeInterface {

    suspend fun getUserRecentChats(uid:String):List<Usuario>

    suspend fun getAllConversationByUid(uidUser:String, uidTalker:String):List<Message>

    suspend fun getUserDataByUid(uid: String):Usuario

    suspend fun getAllContacts(uid: String):List<Usuario>

    suspend fun getLastMessage(uidSender: String, uiReceiver: String):String

    suspend fun postNewMessage(uidReceiver: String, message: Message)

    suspend fun addNewContact(uid: String, email: String, contactName:String)

}