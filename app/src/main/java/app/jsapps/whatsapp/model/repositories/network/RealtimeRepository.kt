package app.jsapps.whatsapp.model.repositories.network

import android.content.ContentValues.TAG
import android.util.Log
import app.jsapps.whatsapp.di.RealtimeModule
import app.jsapps.whatsapp.model.Message
import app.jsapps.whatsapp.model.Usuario
import app.jsapps.whatsapp.utils.toContacts
import app.jsapps.whatsapp.utils.toMessage
import app.jsapps.whatsapp.utils.toUser
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RealtimeRepository @Inject constructor(
    @RealtimeModule.UsersReference private val userInstance: DatabaseReference
) : RealtimeInterface{

    override suspend fun getUserRecentChats(uid: String): List<Usuario> {

        val id = uid.replace('.', ',')
        val userList = mutableListOf<Usuario>()
        return withContext(Dispatchers.IO) {
            userInstance.child(id).child("chats").get().await().children.forEach { snapshot ->
                Log.w(TAG, "key: ${snapshot.key}")
                Log.w(TAG, "data: ${getUserDataByUid(snapshot.key!!)}")
                userList.add(getUserDataByUid(snapshot.key!!))
            }
            userList
        }
    }

    override suspend fun getAllConversationByUid(uidUser: String, uidTalker: String): List<Message> {
        val id = uidUser.replace('.', ',')
        val idTalker = uidTalker.replace('.', ',')
        Log.w(TAG, "user: $id")
        Log.w(TAG, "talker: $idTalker")
        return withContext(Dispatchers.IO){

            userInstance.child(id).child("chats").child(idTalker).get().await().children.map { it.toMessage() }.sortedBy {
                it.timeStamp
            }
        }
    }

    override suspend fun getUserDataByUid(uid: String): Usuario {
        return withContext(Dispatchers.IO) {
            val id = uid.replace('.', ',')
            userInstance.child(id).child("data").get().await().toUser()
        }
    }

    override suspend fun getAllContacts(uid: String): List<Usuario> {
        val userList:MutableList<Usuario>? = null
        val id = uid.replace('.', ',')
        Log.w(TAG, "user: $id")
        /*contacts.also { dataSnapshot ->
            if(dataSnapshot.hasChildren()){
                dataSnapshot.children.forEach {
                    it.key?.let { mailRef -> getUserDataByUid(mailRef) }?.let { dataToUser ->
                        userList?.add(
                            Usuario(nombre = it.child("nombre").value.toString(),
                            email = dataToUser.email,
                            estado = dataToUser.estado,
                            fotoPerfil = dataToUser.fotoPerfil
                        ))
                    }
                }
            }*/


        return withContext(Dispatchers.IO){
            userInstance.child(id).child("contacts").get().await().children.map { snapshot ->
                snapshot.toContacts().also {
                    Log.w(TAG, "user: $it")
                }
            }
        }
    }

    override suspend fun getLastMessage(uidSender: String, uiReceiver: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun postNewMessage(uidReceiver: String, message: Message) {
        val id = message.author.replace('.', ',')
        val idReceiver = uidReceiver.replace('.', ',')
        withContext(Dispatchers.IO){
            userInstance.child(id).child("chats/$idReceiver/${message.timeStamp}").setValue(message)
            userInstance.child(idReceiver).child("chats/$id/${message.timeStamp}").setValue(message)
        }
    }

    override suspend fun addNewContact(uid: String, email: String, contactName:String) {
        val id = uid.replace('.', ',')
        val idReceiver = email.replace('.', ',')
        withContext(Dispatchers.IO){
            userInstance.child(id).child("contacts").child(idReceiver).child("nombre").setValue(contactName)
            userInstance.child(id).child("contacts").child(idReceiver).child("email").setValue(email)
        }
    }
}