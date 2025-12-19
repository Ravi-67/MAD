package com.example.budgettracker

import android.app.AlertDialog
import android.app.KeyguardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.material3.MaterialTheme
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.example.budgettracker.data.local.SecurityPreferences
import com.example.budgettracker.ui.AppRoot
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class MainActivity : FragmentActivity() {

    private lateinit var securityPrefs: SecurityPreferences
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        securityPrefs = SecurityPreferences(this)

        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            checkSecurityAndAuthenticate()
        } else {
            showContent()
        }
    }

    private fun checkSecurityAndAuthenticate() {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (keyguardManager.isDeviceSecure) {
            showBiometricPrompt()
        } else {
            checkInsecureAccessPreference()
        }
    }

    private fun showContent() {
        setContent {
            MaterialTheme {
                AppRoot()
            }
        }
    }

    private fun showBiometricPrompt() {
        val executor: Executor = ContextCompat.getMainExecutor(this)
        val biometricManager = BiometricManager.from(this)

        // Check availability
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(this, "No biometric features available.", Toast.LENGTH_LONG).show()
                checkInsecureAccessPreference()
                return
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(this, "Biometric features unavailable.", Toast.LENGTH_LONG).show()
                checkInsecureAccessPreference()
                return
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(this, "No biometrics enrolled.", Toast.LENGTH_LONG).show()
                // If enrolled is missing but hardware exists, maybe prompt to enroll? 
                // For now, treat as insecure/fallback
                checkInsecureAccessPreference()
                return
            }
        }

        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON || errorCode == BiometricPrompt.ERROR_USER_CANCELED) {
                        finish() // User cancelled, exit app
                    } else {
                        Toast.makeText(applicationContext, "Auth Error: $errString", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    showContent()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(applicationContext, "Auth Failed", Toast.LENGTH_SHORT).show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Budget Tracker Locked")
            .setSubtitle("Unlock with your fingerprint or PIN")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun checkInsecureAccessPreference() {
        lifecycleScope.launch {
            val allowed = securityPrefs.isInsecureAccessAllowed.first()
            if (allowed) {
                showContent()
            } else {
                showInsecureDeviceDialog()
            }
        }
    }

    private fun showInsecureDeviceDialog() {
        AlertDialog.Builder(this)
            .setTitle("Unsecured Device")
            .setMessage("Your device has no lock screen (PIN/Pattern). This means anyone can access your data.\n\nDo you want to proceed anyway?")
            .setPositiveButton("Yes, Proceed") { _, _ ->
                lifecycleScope.launch {
                    securityPrefs.setInsecureAccessAllowed(true)
                    showContent()
                }
            }
            .setNegativeButton("No, Exit") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }
}
