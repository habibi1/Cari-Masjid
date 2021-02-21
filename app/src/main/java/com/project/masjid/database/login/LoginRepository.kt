package com.project.masjid.database.login

import android.R.attr
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.project.masjid.database.model.LoggedInUser
import com.project.masjid.ui.login.LoginActivity
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.lang.Exception
import java.util.*


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    private lateinit var auth: FirebaseAuth
    private lateinit var userLiveData : MutableLiveData<FirebaseUser>

    companion object {
        private const val TAG: String = "Login Status"
    }

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun firebaseSignInEmailAndPassword(
        username: String,
        password: String,
        loginActivity: LoginActivity
    ): ResultLogin<LoggedInUser>{
        auth = Firebase.auth

        return try {
            auth.signInWithEmailAndPassword(username, password).await()
            Log.d(TAG, "signInWithEmail:success")
            val fakeUser = LoggedInUser(UUID.randomUUID().toString(), "Jane Doe")
            ResultLogin.Success(fakeUser)
        } catch (e: Exception) {
            Log.w(TAG, "signInWithEmail:failure")
            ResultLogin.Error(IOException("Error logging in"))
        }
    }

    fun login(username: String, password: String, loginActivity: LoginActivity): ResultLogin<LoggedInUser> {

        // handle login
        val result = dataSource.login(username, password, loginActivity)

        if (result is ResultLogin.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}