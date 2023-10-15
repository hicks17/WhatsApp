package app.jsapps.whatsapp.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.jsapps.whatsapp.model.Message
import app.jsapps.whatsapp.model.Usuario
import app.jsapps.whatsapp.model.repositories.network.RealtimeRepository
import app.jsapps.whatsapp.utils.emptyUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepo: RealtimeRepository
) : ViewModel() {

    private val _user = MutableStateFlow(emptyUser())
    val user = _user.asStateFlow()

    private val _recentChats = MutableStateFlow(emptyList<Usuario>())
    val recentChats = _recentChats.asStateFlow()

    private val _contactList = MutableStateFlow(emptyList<Usuario>())
    val contactList = _contactList.asStateFlow()

    private val _conversation = MutableStateFlow(emptyList<Message>())
    val conversation = _conversation.asStateFlow()

    fun setAllConversations(uid: String) {
        viewModelScope.launch {
            _contactList.update {
                Log.w(TAG, "repo: ${userRepo.getAllContacts(uid)}")
                userRepo.getAllContacts(uid)
            }
        }
    }

    fun getAllConversations(uidUser: String, uidTalker:String) {
        viewModelScope.launch {
            _conversation.update { userRepo.getAllConversationByUid(uidUser, uidTalker) }
        }
    }

    fun getUserData(uid: String) {
        viewModelScope.launch {
            _user.update {
                userRepo.getUserDataByUid(uid)
            }
        }
    }

    fun getRecentChats(uid: String) {
        viewModelScope.launch {
            _recentChats.update {
                userRepo.getUserRecentChats(uid)
            }
        }

    }

    fun addNewContact(uid: String, email: String, contactName:String){
        viewModelScope.launch {
            userRepo.addNewContact(uid, email, contactName)
        }
    }

    fun postMessage(uidReceiver:String, message: Message) {
        viewModelScope.launch {
            userRepo.postNewMessage(uidReceiver, message)
        }
    }
}