package ru.netology.nmedia.auth

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.netology.nmedia.dto.Token


/** -------добавляю для auth-------------------------------------------------------------------- **/
/**
 1.Делаю класс для хранения данных по аунтефикации пользователя
 2.В преференцах делаю два ключа, в одном будет храниться id, в другом токен
 делаю источник данных
 3.делаю обертку для источника данных в папке dto data class Token
 4.применяю init для возможности использования кода в классе
 5.делаю две синхронные функции для удаления и сохранения данных авторизации
 6.настраиваю доступ к классу так, что бы только через функцию getInstance:
  - в классе class AppAuth  делаю приватный конструктор AppAuth private constructor
  - в  companion object  делаю функцию инициализации ( создание, активация, подготовка к работе, определение параметров.)
 fun initAuth(context: Context) и private val INSTANCE = null (ссылка сам на себя)
  - //функция getInstance будет возвращать что я инициализировал
 7.далее делаю новый класс App что бы все инициализировать
 * **/


class AppAuth private constructor(context: Context) {
    //создаю преференс и с помощью context и метода getSharedPreferences("auth", Context.MODE_PRIVATE)
    // "auth" - это имя
    // Context.MODE_PRIVATE - это сособ работы с ними
    private val preferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    //нужен источник данных Изменяемый поток состояний, который предоставляет параметр для значения.
    // Экземпляр MutableStateFlow с заданным начальным value значением может быть создан с помощью
    // MutableStateFlow(value)функции конструктора.
    // При первом входе будет храниться null - MutableStateFlow(null)
    private val _data: MutableStateFlow<Token?> = MutableStateFlow(null)

    //предоставим доступ к свойству private val _data и дадим поток данных, который менять нельзя
    val data = _data.asStateFlow()

    //Основной конструктор не может содержать в себе исполняемого кода. Инициализирующий код может быть
    // помещён в соответствующие блоки (initializers blocks), которые помечаются словом init. При
    // создании экземпляра класса блоки инициализации выполняются в том порядке, в котором они идут в теле класса,
    // с помощью init я смогу что-то записывать в val data
    init {
        //получаю значение. Обязательно!!! Нужно указать значение поу молчанию -> null и 0L
        //значение токена
        val token = preferences.getString(TOKEN_KEY, null)
        //значение ключа
        val id = preferences.getLong(ID_KEY, 0L)

        //добавляю проверку, хватает ли данных в token и key
        if (token == null || id == 0L) {
            //то данные надо почистить. Для это делаю функцию fun removeAuth
            removeAuth()
        } else {
            //если токен и id есть, то мы их записываем
            _data.value = Token(id, token)
        }
    }

    //функция для очистки
    @Synchronized
    fun removeAuth() {
        //обнуляю значение
        _data.value = null
        //SharedPreferencesEditor - Получить объект Editor можно через вызов метода edit объекта SharedPreferences, который вы хотите изменить
        preferences.edit {
            //2вариант
            remove(TOKEN_KEY)
            remove(ID_KEY)

//            //1вариант
//            //поместить в TOKEN_KEY - null
//            putString(TOKEN_KEY, null)
//            //поместить в ID_KEY - 0L
//            putLong(ID_KEY, 0L)
        }
    }

    //функция для запоминания пользователя
    //@Synchronized применяем, что бы смог зайти только один поток и оновить данные
    @Synchronized
    fun saveAuth(id: Long, token: String) {
        //получаю значение
        _data.value = Token(id, token)
        //SharedPreferencesEditor
        preferences.edit {
            //поместить в TOKEN_KEY - token
            putString(TOKEN_KEY, token)
            //поместить в ID_KEY - id
            putLong(ID_KEY, id)
        }
    }

    companion object {
        private const val ID_KEY = "ID_KEY"
        private const val TOKEN_KEY = "TOKEN_KEY"
        //делаю ссылка сам на себя
        // @Volatile которые применяются к полям и гарантируют, что считываемое значение поступает из
        // основной памяти, а не из кэша процессора, поэтому все участники процесса будут ожидать
        // окончания параллельной записи, прежде чем считать значение.
        @Volatile
        private var INSTANCE:AppAuth? = null

        //функция getInstance будет возвращать что я инициализировал
        fun getInstance() = requireNotNull(INSTANCE)

        fun initAuth(context: Context) {
            //создам объект нужного типа
            INSTANCE = AppAuth(context)
        }
    }
}
