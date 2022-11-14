package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.error.AuthorizationException
import ru.netology.nmedia.error.LostConnectException
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl

class AuthSingInViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository =
        PostRepositoryImpl(AppDb.getInstance(context = application).postDao(),
        )
    val authorizationFailed = MutableSharedFlow<Unit>()
    val lostConnection = MutableSharedFlow<Unit>()
    val loginException = MutableSharedFlow<Unit>()
    val loginEvent = MutableSharedFlow<Unit>()

//    init {
//        login(loginInView, passwordInView)
//    }

    fun login(login: String, password: String) {
        viewModelScope.launch {
            try {
            repository.login(login, password)
            } catch (e: LostConnectException) {
                e.printStackTrace()
                lostConnection.emit(Unit)
            } catch (e: AuthorizationException) {
                e.printStackTrace()
                authorizationFailed.emit(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                loginException.emit(Unit)
            }
        }
    }

    fun registration(login: String, password: String, name: String) {
        viewModelScope.launch {
            try {
            repository.registration(login, password, name)
            } catch (e: LostConnectException) {
                e.printStackTrace()
                lostConnection.emit(Unit)
            } catch (e: AuthorizationException) {
                e.printStackTrace()
                authorizationFailed.emit(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                loginException.emit(Unit)
            }
        }
    }


}