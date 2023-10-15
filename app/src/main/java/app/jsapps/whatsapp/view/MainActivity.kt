package app.jsapps.whatsapp.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import app.jsapps.whatsapp.R
import app.jsapps.whatsapp.databinding.ActivityMainBinding
import app.jsapps.whatsapp.utils.Response
import app.jsapps.whatsapp.utils.toast
import app.jsapps.whatsapp.viewmodel.AuthViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private val authVM:AuthViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.fragment_inicio)

        navView.setupWithNavController(navController)

        initFlows()
        binding.logOutBtn.setOnClickListener {
            authVM.logOut()

        }
    }

    private fun initFlows() {
        lifecycleScope.launch {
            authVM.logOutFlow.collect { flowState ->
                when (flowState) {
                    is Response.Success<Boolean> -> {
                        goToLogin()
                    }
                    is Response.Failed -> {
                        showErrorMessage(flowState.exception)
                    }
                    is Response.Loading -> {

                    }
                    else -> Unit
                }
            }
        }
    }

    private fun showErrorMessage(exception: Exception) {
        toast(exception.message!!)
    }

    private fun goToLogin() {
        startActivity(Intent(this, LogInActivity::class.java))
        finish()
    }
}