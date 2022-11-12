package ru.netology.nmedia.application

import android.app.Application
import ru.netology.nmedia.auth.AppAuth

/** -------добавляю для auth-------------------------------------------------------------------- **/
/**
 1.Делаю класс App для инициализации  AppAuth
 2.наследюсь от класса Application - class App:Application(), т.е. получаю доступ к глобальному контексту,
 когда приложение выполняется. Это объект представляет из себя процесс. Когда процесс котов к выполнению, то
 вызовется метод OnCreate и мы сможем инициализировать наш объект  AppAuth.initAuth
 *3.Передаю контекст AppAuth.initAuth(this) в качестве констекста мы передаем этот же класс App, т.к.
 Application является контекстом. И я получаю доступ к файлам, методам и т.д.
 4.Далее нужно сделать, что бы система вызывала эти объекты жизенного цикла в AndroidManifeste:
 <manifest xmlns:android="http://schemas.android.com/apk/res/android"
 package="ru.netology.nmedia">
 <uses-permission android:name="android.permission.INTERNET" />
 <application
 android:name=".application.App"
  *application.App - это я указал имя класса class App : Application() и теперь при старте процесса
  *  вызовется onCreate() и произойдет инициализация объекта из class AppAuth private constructor(context: Context)
  5.Далее иду в interface PostsApiService
 *
 * **/

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppAuth.initAuth(this)
    }
}