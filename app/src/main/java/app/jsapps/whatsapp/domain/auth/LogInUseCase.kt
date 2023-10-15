package app.jsapps.whatsapp.domain.auth

import app.jsapps.whatsapp.model.repositories.network.LoginInterface
import javax.inject.Inject

class LogInUseCase @Inject constructor(
    private val loginRepo:LoginInterface
){

    suspend operator fun invoke(mail:String, pass:String) = loginRepo.logIn(mail, pass)

}
