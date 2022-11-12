package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.navigation.findNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.messaging.FirebaseMessaging
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.FragmentAuthSingInBinding
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.viewmodel.AuthViewModel

/** -------добавляю для auth-------------------------------------------------------------------- **/
/**
1.Добавляю меню с пунктами: войти, зарегистрироваться и выйти:
-если еще не вошли, то увижу регистрацию и вход;
- если вошли, то увижу кнопку выход;
Для этого создаю новый ресурс menu_auth в папке menu
2.Делаю меню и отображение групп из menu_auth когда это нужно -> addMenuProvider(object : MenuProvider
3.Для взаимодействия делаю AuthViewModel, что бы проверить, вошли мы в приложение или нет
4.После создания AuthViewModel Делаем подписку на val data : LiveData<Token?> из нее,
что бы в моменты  изменению меню перерисовывались.
 5.Далее иду в data class Post чтобы добавить информацию, что я являюсь авторм поста
 *
 * **/

class AppActivity : AppCompatActivity(R.layout.activity_app) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.let {
            if (it.action != Intent.ACTION_SEND) {
                return@let
            }

            val text = it.getStringExtra(Intent.EXTRA_TEXT)
            if (text?.isNotBlank() != true) {
                return@let
            }

            intent.removeExtra(Intent.EXTRA_TEXT)
            findNavController(R.id.nav_host_fragment)
                .navigate(
                    R.id.action_feedFragment_to_newPostFragment,
                    Bundle().apply {
                        textArg = text
                    }
                )
        }

        checkGoogleApiAvailability()

        /** auth: 2.Делаю меню и отображение групп из menu_auth когда это нужно **/

        //4.После создания AuthViewModel Делаем подписку на val data
        //вызываю вьюмодель
        val viewModel by viewModels<AuthViewModel>()

        //делаю сыллку на провайдер, что бы скрывать или отображать меню
        var currentMenuProvider: MenuProvider? = null

        //подписываюь на data из AuthViewModel и делаю наблюдателя
        viewModel.data.observe(this) {
            // если currentMenuProvider существует будет наблюдать. Если что-то измениться, то удалим предыдущий провайдер
            // и создастя новый провайдер
            currentMenuProvider?.also { removeMenuProvider(it) }

            //подпишусь на данные, которые предоставляет class AppAuth - это поток данных val data=
            //для этого обращения нужно создать вьюмодель
            //делаю провайдер
            addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    //создаю меню
                    menuInflater.inflate(R.menu.menu_auth, menu)
                    val authorized = viewModel.authorized
                    //буду скрывать и показывать различные группы
                    //если мы вошли в приложение, то покажем пункты authorized
                    menu.setGroupVisible(R.id.authorized, authorized)
                    //если не вошли в приложение, то покажем пункты unauthorized
                    menu.setGroupVisible(R.id.unauthorized, !authorized)

                }

                //override fun onMenuItemSelected - обработка кликов
                override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                    //если id -> (true означает обработать нажатие)
                    when (menuItem.itemId) {
                        R.id.singIn -> {
                            //тут нужны данные пользователя. Для выполнения ДЗ берем данные ->
                            //иду на сервер в controller -> userController:
                            //val netologiay, val sber, ....

                            /** auth: 2.Делаю сам **/
                            //AppAuth.getInstance().saveAuth(5,"x-token")
                            //AppAuth.getInstance().saveAuth(5,"x-token")
                            findNavController(R.id.nav_host_fragment)
                                .navigate(
                                    R.id.action_feedFragment_to_authSingIn,
                                    Bundle())
                            true
                        }
                        R.id.singUp -> {
                            true
                        }
                        R.id.logout -> {
                            AppAuth.getInstance().removeAuth()
                            true
                        }
                        else -> false
                    }
            }.apply {
                currentMenuProvider = this
            })
        }
    }


    /** -------end для auth-------------------------------------------------------------------- **/

    private fun checkGoogleApiAvailability() {
        with(GoogleApiAvailability.getInstance()) {
            val code = isGooglePlayServicesAvailable(this@AppActivity)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }
            if (isUserResolvableError(code)) {
                getErrorDialog(this@AppActivity, code, 9000)?.show()
                return
            }
            Toast.makeText(this@AppActivity, R.string.google_play_unavailable, Toast.LENGTH_LONG)
                .show()
        }

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            println(it)
        }
    }

}