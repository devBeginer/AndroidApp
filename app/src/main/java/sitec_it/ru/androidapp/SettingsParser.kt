package sitec_it.ru.androidapp

import com.google.gson.GsonBuilder
import sitec_it.ru.androidapp.data.models.Settings

class SettingsParser {

    public fun parseSettings(code: String): Settings{

        // Обработка содержания файла
        val gsonDeserializer = GsonBuilder().create()
        val settings: Settings =gsonDeserializer.fromJson(code, Settings::class.java)


        return settings

    }
}