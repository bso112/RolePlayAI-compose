package com.bso112.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.bso112.data.dataStore
import com.bso112.domain.Model
import com.bso112.util.randomID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class AppPreference(
    private val context: Context
) {
    private val dataStore = context.dataStore

    val userId = accessor(stringPreferencesKey("userProfileIdKey"))
    val lastChatLogId = lazyKeyAccessor(defaultValue = randomID)
    val mainPrompt = notNullAccessor(stringPreferencesKey("mainPromptKey"), DEFAULT_MAIN_PROMPT)
    val temperature = notNullAccessor(floatPreferencesKey("temperature"), 0.8f)
    val languageModel =
        notNullAccessor(stringPreferencesKey("languageModelKey"), Model.GPT_3_5.alias)

    private fun <T> accessor(key: Preferences.Key<T>): PreferenceAccessor<T> {
        return PreferenceAccessor(dataStore, key)
    }

    private fun lazyKeyAccessor(defaultValue: String): LazyKeyAccessor {
        return LazyKeyAccessor(dataStore, defaultValue)
    }


    private fun <T> notNullAccessor(
        key: Preferences.Key<T>,
        defaultValue: T
    ): NotNullAccessor<T> {
        return NotNullAccessor(dataStore, key, defaultValue)
    }


}


class PreferenceAccessor<T>(
    private val dataStore: DataStore<Preferences>,
    private val key: Preferences.Key<T>
) {
    suspend fun setValue(value: T) {
        dataStore.edit { it[key] = value }
    }

    suspend fun getValue(): T? {
        return dataStore.data.map { it[key] }.first()
    }

    fun asFlow(): Flow<T?> {
        return dataStore.data.map { it[key] }
    }

    suspend fun getValueOrElse(defaultValue: T): T {
        return dataStore.data.map { it[key] }.first() ?: defaultValue
    }
}

class NotNullAccessor<T>(
    private val dataStore: DataStore<Preferences>,
    private val key: Preferences.Key<T>,
    private val defaultValue: T
) {
    suspend fun setValue(value: T) {
        dataStore.edit { it[key] = value }
    }

    suspend fun getValue(): T {
        return dataStore.data.map { it[key] }.first() ?: defaultValue
    }

    fun asFlow(): Flow<T?> {
        return dataStore.data.map { it[key] }
    }
}

class LazyKeyAccessor(
    private val dataStore: DataStore<Preferences>,
    private val defaultValue: String
) {
    suspend fun setValue(key: String, value: String) {
        dataStore.edit { it[stringPreferencesKey(key)] = value }
    }

    suspend fun getValue(key: String): String {
        return dataStore.data.map { it[stringPreferencesKey(key)] }.first() ?: defaultValue
    }

    fun asFlow(key: String): Flow<String> {
        return dataStore.data.map { it[stringPreferencesKey(key)] ?: defaultValue }
    }
}


private const val DEFAULT_MAIN_PROMPT =
    "You are a world-renowned actor and fanfic writer, specializing in descriptive sentences, brain-teasing plots, and hyperrealistic human-like responses. In this fictional roleplay of {{char}} craft a detailed and immersive experience that showcases your expertise.\n" +
            "\n" +
            "1. Compose a response for {{char}} as they interact with {{user}} in a vivid and engaging manner. Write five to ten paragraphs in an internet RP style\n" +
            "\n" +
            "2. Be proactive and creative in driving the plot and conversation forward. and do the same for the progression of events.\n" +
            "\n" +
            "3. Adhere to the settings and worldview that {{char}} belongs to, while also being prepared for {{user}} to change these at any time. Display your creativity in driving the story forward, but always leave room for {{user}}'s input.\n" +
            "\n" +
            "4. Allow {{char}}'s personality, culture, tone of voice, and other aspects to evolve as the conversation progresses.\n" +
            "\n" +
            "5. Describe using at least two of the following senses: sight, sound, smell, touch, or taste.\n" +
            "\n" +
            "6. Focus on depicting the five senses, paying special attention to sensory details, particularly {{char}}'s appearance – including specific body parts and clothing.\n" +
            "\n" +
            "7. Do not write {{user}}'s responses, break the established worldview, or write messages from {{user}}'s perspective.\n" +
            "\n" +
            "8. What user inputs is mainly {{user}}'s dialogue.\n" +
            "\n" +
            "9. Describe non-dialogues inside asterisks.\n" +
            "\n" +
            "10. Review previous exchanges for context. Ensure you understand all information. Refer to these instructions when crafting responses. Develop and revisit themes. Explore different interaction aspects to cover all elements. Always maintain a craftsmanlike spirit.\n" +
            "\n" +
            "read these guidelines three times, create an unforgettable roleplay experience that showcases your unique talents and transports {{user}} into the captivating world you've crafted."
