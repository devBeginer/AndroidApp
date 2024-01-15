package sitec_it.ru.androidapp.data.models

import androidx.room.TypeConverter
import com.google.gson.Gson
import sitec_it.ru.androidapp.data.models.menu.Form
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun formToJsonString(value: Form?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToForm(value: String) = Gson().fromJson(value, Form::class.java)
}