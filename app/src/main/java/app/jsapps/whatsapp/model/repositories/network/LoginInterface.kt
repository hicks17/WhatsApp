package app.jsapps.whatsapp.model.repositories.network

import app.jsapps.whatsapp.model.Usuario
import app.jsapps.whatsapp.utils.Response
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow

interface LoginInterface {

    suspend fun logIn(mail:String, pass:String): Flow<Response<Boolean>>

    suspend fun signUp(pass:String, data: Usuario): Flow<Response<Boolean>>

    suspend fun logOut(): Flow<Response<Boolean>>

    suspend fun updateName(scoreType: String, uid: String, score: Int) : Boolean

    suspend fun getUserDataByMail(mail: String) : DataSnapshot?

    //suspend fun getUserDataByRoom(typeData:Int) : LocalStatsEntity2

    suspend fun getSingleUserDataByRoom(typeRec: String, backOrNot:Int): Int

    //suspend fun updateDataByRoom(userStatsEntity: LocalStatsEntity2)

    //suspend fun setRoomUserData(udata: LocalStatsEntity2)

    suspend fun deleteRoomData()

    suspend fun getSingleRecordByUid(uid: String, typeRec:String) : DataSnapshot?

}