package app.jsapps.whatsapp.model.repositories.network

import android.content.ContentValues.TAG
import android.util.Log
import app.jsapps.whatsapp.di.RealtimeModule
import app.jsapps.whatsapp.model.Usuario
import app.jsapps.whatsapp.utils.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginImpl @Inject constructor(
    private val auth: FirebaseAuth,
    //private val statsDao: LocalStatsDao,
    @RealtimeModule.UsersReference private val userInstance: DatabaseReference
) : LoginInterface {
    override suspend fun logIn(mail: String, pass: String) = flow {
        emit(Response.Loading)
        try {

            var response = false

            auth.signInWithEmailAndPassword(mail, pass)
                .addOnSuccessListener { response = true }
                .addOnFailureListener { response = false }
                .await()

            Log.w(TAG, "response: $response")
            emit(Response.Success(response))
            emit(Response.Finish)

        } catch (e: Exception) {
            emit(Response.Failed(e))
            Log.w(TAG, "response: ${e.localizedMessage}")
            emit(Response.Finish)
        }
    }

    override suspend fun signUp(pass: String, data: Usuario) = flow {
        emit(Response.Loading)
        try {
            var response = false
            lateinit var exception: Exception
            auth.createUserWithEmailAndPassword(data.email, pass).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    response = true
                    val id = data.email.replace('.', ',')
                    Log.w(TAG, id)
                    userInstance.child(id).child("data").setValue(data)
                } else {
                    exception = task.exception!!
                    response = false
                }
            }
                .await()
            emit(Response.Success(response))
            emit(Response.Finish)
        } catch (e: Exception) {
            emit(Response.Failed(e))
            emit(Response.Finish)
        }
    }

    override suspend fun logOut() = flow {
        emit(Response.Loading)
        auth.signOut()
        emit(Response.Success(true))
    }

    override suspend fun updateName(scoreType: String, uid: String, score: Int): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getUserDataByMail(mail: String): DataSnapshot? {
        TODO("Not yet implemented")
    }

    override suspend fun getSingleUserDataByRoom(typeRec: String, backOrNot: Int): Int {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRoomData() {
        TODO("Not yet implemented")
    }

    override suspend fun getSingleRecordByUid(uid: String, typeRec: String): DataSnapshot? {
        TODO("Not yet implemented")
    }
}