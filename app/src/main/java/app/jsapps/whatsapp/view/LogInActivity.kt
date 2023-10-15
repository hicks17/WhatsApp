package app.jsapps.whatsapp.view

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import app.jsapps.whatsapp.R
import app.jsapps.whatsapp.databinding.ActivityLogInBinding
import app.jsapps.whatsapp.utils.Response
import app.jsapps.whatsapp.utils.toast
import app.jsapps.whatsapp.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding

    private val authVM: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFlows()
        initListeners()
    }

    private fun initListeners() {
        binding.loginbtn.setOnClickListener {

            val correo = binding.etEmail.text.toString()
            val contra = binding.etContra.text.toString()

            if (correo.isEmpty() || contra.isEmpty()) {

                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()

            } else {

                loginUser(correo, contra)


            }

        }

        binding.signInBtn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)

        }
    }

    private fun loginUser(correo: String, contra: String) {
        authVM.login(correo, contra)
    }

    private fun showProgressBar() {
        binding.progress.isVisible = true
    }

    private fun initFlows() {

        this.lifecycleScope.launch {
            authVM.loginState.collect { flowState ->
                Log.d(TAG, "flow: $flowState")
                when (flowState) {
                    is Response.Success<Boolean> -> {
                        Log.w(TAG, "succes")
                        binding.progress.isVisible = false
                        updateUI()

                    }
                    is Response.Failed -> {
                        Log.w(TAG, "failed")
                        showErrorMessage(flowState.exception)
                        binding.progress.isVisible = false
                    }
                    is Response.Loading -> {
                        Log.w(TAG, "cargando")
                        showProgressBar()
                    }
                    else -> Unit
                }
            }
        }
    }


    private fun showErrorMessage(e: Exception) {
        toast("Error: ${e.localizedMessage}")
    }


    private fun updateUI() {
        val intent = Intent(this@LogInActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {

    }

}