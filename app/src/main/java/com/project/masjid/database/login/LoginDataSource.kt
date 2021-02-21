package com.project.masjid.database.login

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.project.masjid.database.model.LoggedInUser
import com.project.masjid.ui.login.LoginActivity
import java.io.IOException
import java.util.*

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    private lateinit var auth: FirebaseAuth

    fun login(username: String, password: String, loginActivity: LoginActivity): ResultLogin<LoggedInUser> {
        try {
            auth = Firebase.auth
            var status = false

            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(loginActivity) { task ->
                    status = if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        //val user = auth.currentUser
                        true
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        false
                        // ...
                    }

                    // ...
                }

            return if (status){
                Log.d(TAG, "signInWithEmail:success")
                val fakeUser = LoggedInUser(UUID.randomUUID().toString(), "Jane Doe")
                ResultLogin.Success(fakeUser)
            } else {
                Log.w(TAG, "signInWithEmail:failure")
                ResultLogin.Error(IOException("Error logging in"))
            }

        } catch (e: Throwable) {
            Log.w(TAG, "signInWithEmail:failure", e)
            return ResultLogin.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }

    companion object {
        private const val TAG: String = "Login Status"
    }
}