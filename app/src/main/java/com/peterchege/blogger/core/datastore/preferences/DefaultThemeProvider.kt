package com.peterchege.blogger.core.datastore.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import javax.inject.Inject

enum class ThemeConfig {
    FOLLOW_SYSTEM,
    LIGHT,
    DARK,
}
class DefaultThemeProvider @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {


}