package com.example.tadeos.data.repository

import android.content.Intent
import com.facebook.CallbackManager

object FacebookAuthBridge {
    var callbackManager: CallbackManager? = null

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return callbackManager?.onActivityResult(requestCode, resultCode, data) == true
    }
}
