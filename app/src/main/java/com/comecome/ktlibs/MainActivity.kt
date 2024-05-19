package com.comecome.ktlibs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.comecome.ktlibs.databinding.ActivityMainBinding
import com.comecome.ktutils.BiometricAuthListener

import com.comecome.ktutils.Utils

class MainActivity : AppCompatActivity(), BiometricAuthListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btAuth.setOnClickListener {
            Utils.authBio(
                "Autenticar com Biometria",
                "Toque no sensor",
                "Use sua biometria para garantir que é você mesmo!",
                "CANCELAR",
                this,
                this)
        }

    }

    override fun onBiometricAuthenticateError(errMsg: String) {
        Log.d("biometria","Erro: $errMsg")
        Utils.displayMessage("Erro: $errMsg", this)
    }

    override fun onBiometricAuthenticateSuccess() {
        Log.d("biometria","Autenticado com sucesso!")
        Utils.displayMessage("Autenticado com sucesso!", this)
    }
}

