package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.utils

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

internal sealed class AppStoragePreferencesKeys<T>(val key: Preferences.Key<T>) {
	object BuildURLKey : AppStoragePreferencesKeys<String>(stringPreferencesKey("build_url_key"))
	object ParentKey : AppStoragePreferencesKeys<String>(stringPreferencesKey("parent_key"))
	object DatabaseURLKey : AppStoragePreferencesKeys<String>(stringPreferencesKey("database_url_key"))
}