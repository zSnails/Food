package com.zsnails.food.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.zsnails.food.BuildConfig
import com.zsnails.food.MainActivity
import com.zsnails.food.databinding.ActivityLoginBinding
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var client: SupabaseClient
    private var user: UserInfo? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        client = createSupabaseClient(BuildConfig.SUPABASE_URL, BuildConfig.SUPABASE_KEY) {
            install(Postgrest)
            install(Auth)
        }
        user = client.auth.currentUserOrNull()
        user.let {
            // TODO: store the user in some global state, but I think I can just use the global user stored in supabase
            goToMain()
        }
    }

    private fun goToMain() {
        val intent = Intent(baseContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun doLogin(view: View) {
        val mail = binding.emailAddress.text.toString()
        val pass = binding.password.text.toString()

        lifecycleScope.launch {
            try {
                client.auth.signInWith(Email) {
                    email = mail
                    password = pass
                }
                Log.d("AUTH:LOGIN", "Sucessfully signed in")
                goToMain()
            } catch (a: RestException) {
                Log.e("AUTH:ERR", String.format("could not sign in: %s", a.error))
            }
        }
    }
}