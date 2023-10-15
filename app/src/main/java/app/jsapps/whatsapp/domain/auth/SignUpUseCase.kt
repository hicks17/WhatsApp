package app.jsapps.whatsapp.domain.auth

import app.jsapps.whatsapp.model.Usuario
import app.jsapps.whatsapp.model.repositories.network.LoginInterface
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val loginRepo: LoginInterface
){

    suspend operator fun invoke(pass:String, user:Usuario) = loginRepo.signUp(pass, user)

}
