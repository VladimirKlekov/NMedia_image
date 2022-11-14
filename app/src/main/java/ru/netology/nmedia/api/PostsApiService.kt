package ru.netology.nmedia.api

import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.dto.Media
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.Token

/** -------добавляю для auth-------------------------------------------------------------------- **/
/**
Вношу изменения в функции api для аунтефикаци:
I Вариант: добавить в функции слово @Header (Задаёт все заголовки вместе), например suspend fun getNewer(@Header

II Вариант: что бы везде не вписывать @Header, можно использовать интерсептеры (Interceptor - это перехватчик)
В библиотеку можно внедрить перехватчики для изменения заголовков при помощи класса Interceptor из OkHttp.
Сначала следует создать объект перехватчика и передать его в OkHttp, который в свою очередь следует
явно подключить в Retrofit.Builder через метод client(). Поддержка перехватчиков/interceptors для
обработки заголовков запросов, например, для работы с токенами авторизации в заголовке Authorization.
1. Создаю свой interceptor private val authInterceptor = object : Interceptor
2. Добавляю okhttp клиент в private val okhttp = OkHttpClient.Builder() ->.addInterceptor(authInterceptor)
и получаю доступ к цепочке действий
P.S. Цепочка действий является одной из форм паттерна Builder(Строитель). Вместо непосредственного
создания желаемого объекта, клиент вызывает конструктор (или статическую фабрику) со всеми необходимыми
параметрами и получает объект строителя. Затем клиент вызывает сеттер-подобные методы у объекта строителя
для установки каждого дополнительного параметра. ... Строитель является статическим внутренним классом в
классе, который он строит.
3. Теперь каждому клиенту будет идти доп информация. Так как мы использовали клиент .addInterceptor(authInterceptor)
при создании retrofita, а retrofit испьзуется для создания ApiService, то кождый из методов в нем будет
 не яво отравлять заголовок об авторизации
 4.Следующий шаг -> записать что-то в объект авторизации class AppAuth:
 - нужно добавить меню в AppActivity ->
 5. Доплолнил интерфей
@FormUrlEncoded
@POST("users/authentication")
suspend fun updateUser(@Field("login") login: String, @Field("pass") pass: String): Response<ваш_тип>



 *
 * **/

private const val BASE_URL = "${BuildConfig.BASE_URL}/api/slow/"

private val logging = HttpLoggingInterceptor().apply {
    if (BuildConfig.DEBUG) {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

//1. Создаю свой interceptor
private val authInterceptor = Interceptor { chain ->
    //Сюда приходит цепочка из действий. Их можно комбинировать или добавлять в рамках одного okhttp клиента
    //модифицируем цепочку данных по аунтефикаци
    val request = AppAuth.getInstance().data.value?.token?.let {
//если токен есть
        chain.request()
            //добавляю заголовок
            .newBuilder()
            //добавляю заголов автоизации -> "Authorization"
            .addHeader("Authorization", it)
            .build()
        //если токена нет, то мы возьмем из цепочки запрос такой, какой он есть ->
    } ?: chain.request()
    //получаем ответ -> результат работы интерсептера
    chain.proceed(request)
}


//2. Добавляю okhttp клиент ->.addInterceptor(authInterceptor)
private val okhttp = OkHttpClient.Builder()
    .addInterceptor(logging)
    .addInterceptor(authInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(okhttp)
    .build()

interface PostsApiService {
//    @Multipart
//    @POST("users/registration")
//    suspend fun registerWithPhoto(
//        @Part("login") login: RequestBody,
//        @Part("pass") pass: RequestBody,
//        @Part("name") name: RequestBody,
//        @Part media: MultipartBody.Part,
//    ): Response<Token>

    @FormUrlEncoded
    @POST("users/authentication")
    suspend fun updateUser(@Field("login") login: String, @Field("pass") pass: String): Response<Token>

    @FormUrlEncoded
    @POST("users/registration")
    suspend fun registerUser(@Field("login") login: String, @Field("pass") pass: String, @Field("name") name: String): Response<Token>

    @GET("posts")
    suspend fun getAll(): Response<List<Post>>

    @GET("posts/{id}/newer")
    suspend fun getNewer(@Path("id") id: Long): Response<List<Post>>

    @GET("posts/{id}")
    suspend fun getById(@Path("id") id: Long): Response<Post>

    @POST("posts")
    suspend fun save(@Body post: Post): Response<Post>

    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Long): Response<Unit>

    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Response<Post>

    @DELETE("posts/{id}/likes")
    suspend fun unlikeById(@Path("id") id: Long): Response<Post>

    @Multipart
    @POST("media")
    //suspend fun upload(@Part media: MultipartBody.Part): Response<Media>

    // вернуть две чатси?

    suspend fun upload(
        @Part part: MultipartBody.Part,
        @Part content: MultipartBody.Part,
    ): Response<Media>
}

object PostsApi {
    val service: PostsApiService by lazy {
        retrofit.create(PostsApiService::class.java)
    }

    /** -------добавляю для auth---------------------------------------------------------------- **/

}