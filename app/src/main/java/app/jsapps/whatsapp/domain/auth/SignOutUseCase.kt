package app.jsapps.whatsapp.domain.auth

import app.jsapps.whatsapp.model.repositories.network.LoginImpl
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val repository: LoginImpl
){


suspend operator fun invoke() = repository.logOut()
}
