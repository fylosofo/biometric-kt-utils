package com.comecome.ktutils

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat


class Utils {

    companion object {
        private fun checkBiometricSupport(
            activity: AppCompatActivity,
            listener: BiometricAuthListener,
            promptInfo: BiometricPrompt.PromptInfo
        ) {
            if(activity.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
                // displayMessage("Dispositivo com suporte a biometria!", activity)
                when(BiometricManager.from(activity).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
                    BiometricManager.BIOMETRIC_SUCCESS -> {
                        // displayMessage("Biometria cadastrada", activity)
                        initBiometricPrompt(activity, listener, promptInfo)
                    }
                    BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                        displayMessage("Dispositivo sem suporte a biometria!", activity)
                    }
                    BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                        displayMessage("O hardware de biometria não está disponível no momento.", activity)
                    }
                    BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                        displayMessage("Dispositivo sem biometria cadastrada!", activity)
                    }
                }
            } else {
                displayMessage("Dispositivo sem suporte a biometria!", activity)
            }
        }

        //setting up a biometric
        private fun setBiometricPromptInfo(
            title: String,
            subtitle: String,
            description: String,
            cancelText: String
        ): BiometricPrompt.PromptInfo {
            return BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setDescription(description)
                .setNegativeButtonText(cancelText)
                .build()
        }

        /*
         * Initiate the Biometric Prompt
         */
        private fun initBiometricPrompt(
            activity: AppCompatActivity,
            listener: BiometricAuthListener,
            promptInfo: BiometricPrompt.PromptInfo
        ) {

            val executor = ContextCompat.getMainExecutor(activity)
            val callback = object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    listener.onBiometricAuthenticateError(errString.toString())
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    listener.onBiometricAuthenticateSuccess()
                }
            }
            val biometricPrompt = BiometricPrompt(activity, executor, callback)
            biometricPrompt.authenticate(promptInfo)
        }

        /*
         * Display the Biometric Prompt
        */
        fun authBio(
            title: String = "Autenticar com Biometria",
            subtitle: String = "Toque no sensor",
            description: String = "Use sua biometria para garantir que é você mesmo!",
            cancelText: String = "Cancelar",
            activity: AppCompatActivity,
            listener: BiometricAuthListener,
        ) {
            val promptInfo = setBiometricPromptInfo(
                title,
                subtitle,
                description,
                cancelText,
            )

            checkBiometricSupport(activity, listener, promptInfo)
        }

        fun displayMessage(message: String, activity: AppCompatActivity) {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
    }
}

interface BiometricAuthListener {
    fun onBiometricAuthenticateError(errMsg: String)
    fun onBiometricAuthenticateSuccess()
}