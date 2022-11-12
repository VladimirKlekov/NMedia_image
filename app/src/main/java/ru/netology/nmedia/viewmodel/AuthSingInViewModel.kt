package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl

class AuthSingInViewModel (application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository =
        PostRepositoryImpl(AppDb.getInstance(context = application).postDao())
    private val loginInView: String = ""
    var passwordInView: String = ""

    init {
        login(loginInView, passwordInView)
    }

    fun login(login: String, password: String) {
        viewModelScope.launch {
            loginInView == login
            passwordInView == password

                repository.login(login, password)

        }
    }


}