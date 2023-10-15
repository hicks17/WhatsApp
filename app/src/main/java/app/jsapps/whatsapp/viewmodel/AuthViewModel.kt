package app.jsapps.whatsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.jsapps.whatsapp.domain.auth.AuthUseCases
import app.jsapps.whatsapp.model.Usuario
import app.jsapps.whatsapp.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    //private val userRepository: UserRepository
) : ViewModel() {

    private val _loginState : MutableSharedFlow<Response<Boolean>> = MutableSharedFlow()
    val loginState : SharedFlow<Response<Boolean>> = _loginState.asSharedFlow()

    private val _signUpFlow : MutableSharedFlow<Response<Boolean>> = MutableSharedFlow()
    val signUpFlow : SharedFlow<Response<Boolean>> = _signUpFlow.asSharedFlow()

    private val _logOutFlow : MutableStateFlow<Response<Boolean>> = MutableStateFlow(Response.Finish)
    val logOutFlow : StateFlow<Response<Boolean>> = _logOutFlow.asStateFlow()

    fun login(mail:String, pass:String){
        viewModelScope.launch {
            authUseCases.logInUseCase(mail, pass)
                .onEach { flowData ->
                    _loginState.emit(flowData)
                }.launchIn(viewModelScope)
        }
    }

    fun signUp(user:Usuario, pass: String){
        viewModelScope.launch {
            authUseCases.signUpUseCase(pass, user)
                .onEach { signUpState ->
                    _signUpFlow.emit(signUpState)
                }.launchIn(viewModelScope)
        }
    }

    fun logOut() {
        viewModelScope.launch {
            authUseCases.signOutUseCase().onEach { logoutState ->
                _logOutFlow.update { logoutState }
            }.launchIn(viewModelScope)
        }
    }

}