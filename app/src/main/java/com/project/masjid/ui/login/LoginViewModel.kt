package com.project.masjid.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.masjid.R
import com.project.masjid.database.login.LoginRepository
import com.project.masjid.database.login.ResultLogin

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    suspend fun login(username: String, password: String, loginActivity: LoginActivity) {

        _isLoading.value = true

        // can be launched in a separate asynchronous job
        when (val result = loginRepository.firebaseSignInEmailAndPassword(
            username,
            password,
            loginActivity
        )){
            is ResultLogin.Success -> {
                _isLoading.value = false
                _loginResult.value =
                    LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
            }
            is ResultLogin.Error -> {
                _isLoading.value = false
                _loginResult.value = LoginResult(error = R.string.login_failed)
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}