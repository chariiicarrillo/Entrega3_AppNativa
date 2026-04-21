package com.tadeos.app.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.facebook.AccessToken
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

data class SocialAuthResult(
    val success: Boolean,
    val message: String? = null
)

object SocialAuthRepository {
    suspend fun signInWithGoogle(context: Context): SocialAuthResult {
        val webClientId = defaultWebClientId(context)
            ?: return SocialAuthResult(
                success = false,
                message = "Falta default_web_client_id. Habilita Google en Firebase, agrega SHA-1/SHA-256 y descarga de nuevo google-services.json."
            )

        return try {
            val credentialManager = CredentialManager.create(context)
            val googleOption = GetSignInWithGoogleOption.Builder(webClientId).build()
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleOption)
                .build()
            val response = credentialManager.getCredential(
                context = context,
                request = request
            )
            val credential = response.credential

            if (credential !is CustomCredential) {
                return SocialAuthResult(
                    success = false,
                    message = "No pudimos leer la cuenta de Google seleccionada."
                )
            }

            val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val firebaseCredential = GoogleAuthProvider.getCredential(
                googleCredential.idToken,
                null
            )

            authenticateWithFirebase(firebaseCredential)
        } catch (exception: GoogleIdTokenParsingException) {
            SocialAuthResult(
                success = false,
                message = "No pudimos validar la cuenta de Google seleccionada."
            )
        } catch (exception: GetCredentialException) {
            SocialAuthResult(
                success = false,
                message = googleCredentialMessage(exception)
            )
        } catch (exception: Exception) {
            SocialAuthResult(
                success = false,
                message = firebaseAuthMessage(exception, "Google")
            )
        }
    }

    fun signInWithFacebookToken(
        accessToken: AccessToken,
        onComplete: (SocialAuthResult) -> Unit
    ) {
        val credential = FacebookAuthProvider.getCredential(accessToken.token)

        FirebaseAuth.getInstance()
            .signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    TadeosFirebaseRepository.saveCurrentProviderProfile { success, message ->
                        onComplete(
                            SocialAuthResult(
                                success = success,
                                message = message
                            )
                        )
                    }
                } else {
                    onComplete(
                        SocialAuthResult(
                            success = false,
                            message = firebaseAuthMessage(task.exception, "Facebook")
                        )
                    )
                }
            }
    }

    private suspend fun authenticateWithFirebase(
        credential: AuthCredential
    ): SocialAuthResult = suspendCancellableCoroutine { continuation ->
        FirebaseAuth.getInstance()
            .signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (!continuation.isActive) {
                    return@addOnCompleteListener
                }

                if (!task.isSuccessful) {
                    continuation.resume(
                        SocialAuthResult(
                            success = false,
                            message = firebaseAuthMessage(task.exception, "Google")
                        )
                    )
                    return@addOnCompleteListener
                }

                TadeosFirebaseRepository.saveCurrentProviderProfile { success, message ->
                    if (continuation.isActive) {
                        continuation.resume(
                            SocialAuthResult(
                                success = success,
                                message = message
                            )
                        )
                    }
                }
            }
    }

    private fun defaultWebClientId(context: Context): String? {
        val resourceId = context.resources.getIdentifier(
            "default_web_client_id",
            "string",
            context.packageName
        )

        return if (resourceId == 0) {
            null
        } else {
            context.getString(resourceId).takeIf { it.isNotBlank() }
        }
    }

    private fun googleCredentialMessage(exception: GetCredentialException): String {
        val typeName = exception::class.simpleName.orEmpty()
        val rawMessage = exception.message.orEmpty()

        return when {
            typeName.contains("Cancellation", ignoreCase = true) ||
                rawMessage.contains("cancel", ignoreCase = true) -> {
                "Inicio de sesion cancelado."
            }
            typeName.contains("NoCredential", ignoreCase = true) -> {
                "No encontramos una cuenta de Google disponible en este dispositivo."
            }
            rawMessage.contains("network", ignoreCase = true) -> {
                "Revisa tu conexion a internet e intenta de nuevo."
            }
            else -> {
                "No pudimos iniciar sesion con Google. Revisa que el proveedor este activo en Firebase."
            }
        }
    }

    private fun firebaseAuthMessage(exception: Exception?, providerName: String): String {
        val rawMessage = exception?.localizedMessage.orEmpty()

        return when {
            rawMessage.contains("CONFIGURATION_NOT_FOUND", ignoreCase = true) ||
                rawMessage.contains("configuration", ignoreCase = true) -> {
                "Activa $providerName en Firebase Authentication y verifica la configuracion del proyecto."
            }
            rawMessage.contains("network", ignoreCase = true) -> {
                "Revisa tu conexion a internet e intenta de nuevo."
            }
            rawMessage.contains("disabled", ignoreCase = true) -> {
                "Esta cuenta esta deshabilitada."
            }
            rawMessage.contains("credential", ignoreCase = true) ||
                rawMessage.contains("malformed", ignoreCase = true) ||
                rawMessage.contains("expired", ignoreCase = true) -> {
                "No pudimos validar la cuenta de $providerName. Intenta iniciar sesion de nuevo."
            }
            rawMessage.isNotBlank() -> rawMessage
            else -> "No pudimos iniciar sesion con $providerName."
        }
    }
}
