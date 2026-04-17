package com.example.tadeos.ui.screens.auth

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.example.tadeos.R
import com.example.tadeos.data.repository.FacebookAuthBridge
import com.example.tadeos.data.repository.SocialAuthRepository
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.coroutines.launch

data class SocialAuthActions(
    val signInWithGoogle: () -> Unit,
    val signInWithFacebook: () -> Unit
)

@Composable
fun rememberSocialAuthActions(
    onAuthSuccess: () -> Unit,
    onError: (String) -> Unit,
    onLoadingChange: (Boolean) -> Unit
): SocialAuthActions {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }
    val scope = rememberCoroutineScope()
    val callbackManager = remember { CallbackManager.Factory.create() }
    val latestSuccess = rememberUpdatedState(onAuthSuccess)
    val latestError = rememberUpdatedState(onError)
    val latestLoading = rememberUpdatedState(onLoadingChange)

    DisposableEffect(callbackManager) {
        FacebookAuthBridge.callbackManager = callbackManager

        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    latestLoading.value(true)
                    SocialAuthRepository.signInWithFacebookToken(result.accessToken) { authResult ->
                        latestLoading.value(false)
                        if (authResult.success) {
                            latestSuccess.value()
                        } else {
                            latestError.value(
                                authResult.message ?: "No pudimos iniciar sesion con Facebook."
                            )
                        }
                    }
                }

                override fun onCancel() {
                    latestLoading.value(false)
                    latestError.value("Inicio de sesion cancelado.")
                }

                override fun onError(error: FacebookException) {
                    latestLoading.value(false)
                    latestError.value(
                        error.localizedMessage ?: "No pudimos iniciar sesion con Facebook."
                    )
                }
            }
        )

        onDispose {
            if (FacebookAuthBridge.callbackManager === callbackManager) {
                FacebookAuthBridge.callbackManager = null
            }
            LoginManager.getInstance().unregisterCallback(callbackManager)
        }
    }

    return remember(context, activity, scope) {
        SocialAuthActions(
            signInWithGoogle = {
                latestLoading.value(true)
                scope.launch {
                    val result = SocialAuthRepository.signInWithGoogle(context)
                    latestLoading.value(false)

                    if (result.success) {
                        latestSuccess.value()
                    } else {
                        latestError.value(
                            result.message ?: "No pudimos iniciar sesion con Google."
                        )
                    }
                }
            },
            signInWithFacebook = facebookSignIn@{
                val configError = facebookConfigError(context)
                if (configError != null) {
                    latestError.value(configError)
                    return@facebookSignIn
                }

                if (activity == null) {
                    latestError.value("No pudimos abrir el inicio de sesion de Facebook.")
                    return@facebookSignIn
                }

                latestLoading.value(true)
                LoginManager.getInstance().logInWithReadPermissions(
                    activity,
                    listOf("email", "public_profile")
                )
            }
        )
    }
}

private fun facebookConfigError(context: Context): String? {
    val appId = context.getString(R.string.facebook_app_id)
    val clientToken = context.getString(R.string.facebook_client_token)

    return when {
        appId == "000000000000000" -> {
            "Configura FACEBOOK_APP_ID en local.properties antes de usar Facebook."
        }
        clientToken == "REEMPLAZA_FACEBOOK_CLIENT_TOKEN" -> {
            "Configura FACEBOOK_CLIENT_TOKEN en local.properties antes de usar Facebook."
        }
        else -> null
    }
}

private tailrec fun Context.findActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> null
    }
}
