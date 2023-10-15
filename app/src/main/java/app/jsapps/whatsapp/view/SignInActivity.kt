package app.jsapps.whatsapp.view

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import app.jsapps.whatsapp.databinding.ActivitySignInBinding
import app.jsapps.whatsapp.model.Usuario
import app.jsapps.whatsapp.utils.Response
import app.jsapps.whatsapp.utils.toast
import app.jsapps.whatsapp.viewmodel.AuthViewModel
import app.jsapps.whatsapp.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.regex.Pattern

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding

    private val userVM: UserViewModel by viewModels()
    private val authVM: AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFlows()
        initListeners()
    }

    private fun initFlows() {
            lifecycleScope.launch {
                authVM.signUpFlow.collect { flowState ->
                    when (flowState) {
                        is Response.Success<Boolean> -> {
                            crearCuenta()
                        }
                        is Response.Failed -> {
                            showErrorMessage(flowState.exception)
                        }
                        is Response.Loading -> {
                            showProgressBar()
                        }
                        else -> Unit
                    }
            }
        }
    }

    private fun showProgressBar() {
        binding.progress.visibility = View.VISIBLE
    }

    private fun showErrorMessage(exception: Exception) {
        toast("error: $exception")
    }

    private fun initListeners() {
        binding.signInBtn.setOnClickListener {
            val correo = binding.etEmail.text.toString()
            val contra = binding.etContra.text.toString()
            val nombre = binding.etNombre.text.toString()
            val confirmPass = binding.etConfirmPass.text.toString()

            val passwordRegex = Pattern.compile("^" +
                    "(?=.*[$&!¡+=-@^])" +
                    ".{6,}" +
                    "$")

            if(correo.isEmpty()){
                toast("Ingrese un correo")

            }else if(nombre.isEmpty()){

                toast("Ingrese un nombre")

            }else if(!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){

                toast("Ingrese un email válido")

            }else if(contra.isEmpty()){

                toast("Ingrese una contraseña")
            }else if (!passwordRegex.matcher(contra).matches()){

                toast("La contraseña es muy débil")
            }else if(contra != confirmPass){

                toast("Las contraseñas no coinciden")
            }else{
                val usuario = Usuario(nombre, correo)
                authVM.signUp(usuario, contra)

            }

        }

        binding.ivInfo.setOnClickListener { toast("La contraseña requiere un mínimo de 6 caracteres y al menos un caracter especial") }
    }

    private fun crearCuenta() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)

    }


}