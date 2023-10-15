package app.jsapps.whatsapp.domain.auth

import javax.inject.Inject

data class AuthUseCases @Inject constructor(
    val logInUseCase:LogInUseCase,
    val signUpUseCase:SignUpUseCase,
    val signOutUseCase:SignOutUseCase
)