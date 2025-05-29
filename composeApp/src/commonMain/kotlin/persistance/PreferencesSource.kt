package persistance

import java.util.prefs.Preferences

internal object PreferencesSource {

    private val preferences: Preferences by lazy { Preferences.userRoot().node(this.javaClass.name) }

    fun putBoolean(key: String, value: Boolean) = preferences.putBoolean(key, value)

    fun getBoolean(key: String, default: Boolean): Boolean = preferences.getBoolean(key, default)

    fun putString(key: String, value: String) = preferences.put(key, value)

    fun getString(key: String, default: String): String = preferences.get(key, default)
}

/*
internal object QAToolPreferenceTest {

    fun testPreferencesWrite() {
        try {
            // Obtain a preferences node specific to your package name
            val prefs = Preferences.userRoot().node(this.javaClass.name)
            println("trigger preferences create success")
            // Store preferences
            prefs.putBoolean("darkMode", true)
            println("trigger preferences put success")
        } catch (e: Exception) {
            println("trigger preferences exception: $e")
        }
    }

    fun testPreferencesRead() {
        try {
            // Obtain a preferences node specific to your package name
            val prefs = Preferences.userRoot().node(this.javaClass.name)
            println("trigger preferences2 create success")
            // Store preferences
            val darkMode = prefs.getBoolean("darkMode", false)
            println("trigger preferences get success: $darkMode")
        } catch (e: Exception) {
            println("trigger preferences exception: $e")
        }
    }
}
 */
