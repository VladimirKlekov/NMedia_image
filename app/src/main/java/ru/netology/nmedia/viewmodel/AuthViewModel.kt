package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.Dispatchers
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.Token


/** -------добавляю для auth-------------------------------------------------------------------- **/
/**
1.Делаю AuthViewModel что бы проверить, вошли мы в приложение или нет
 2. Делаю переменную al authorized :Boolean для проверки изапроса
 3.Делаю подписку val data : LiveData<Token?>
 4.Делаем подписку в AppActivity на val data : LiveData<Token?>, что бы в моменты  изменению меню перерисовывались
 **/

class AuthViewModel:ViewModel() {

//делаю подписку. Буду возвращать токен
    val data : LiveData<Token?> = AppAuth.getInstance().data.asLiveData(Dispatchers.Default)

    val authorized :Boolean
    //проверяю что токен не равен null. Делаю через геттер, что бы информация запрашивалась каждый раз
    //иначе будет баг при создании вьюмодели: начальное состояние сохранилось, а все остальные бы не отобразились
    get() = AppAuth.getInstance().data.value?.token != null


}