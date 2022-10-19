package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.data.source.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.utils.AppStoragePreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

internal class AppStoragePreferencesDataStore(context: Context) {
	companion object {
		val Context.appStoragePreferences by preferencesDataStore("app_storage_preferences_data_store")
	}

	private val preferences = context.appStoragePreferences

	suspend fun setParentKey(value: String) {
		preferences.edit { it[AppStoragePreferencesKeys.ParentKey.key] = value }
	}

	suspend fun getParentKey(): String {
		return preferences.data.first()[AppStoragePreferencesKeys.ParentKey.key] ?: ""
	}

	suspend fun setDatabaseURLKey(value: String) {
		preferences.edit { it[AppStoragePreferencesKeys.DatabaseURLKey.key] = value }
	}

	suspend fun getDatabaseURLKey(): String {
		return preferences.data.first()[AppStoragePreferencesKeys.DatabaseURLKey.key] ?: ""
	}

	suspend fun setBuildURLKey(value: String) {
		preferences.edit { it[AppStoragePreferencesKeys.BuildURLKey.key] = value }
	}

	suspend fun getBuildURLKey(): String {
		return preferences.data.first()[AppStoragePreferencesKeys.BuildURLKey.key] ?: ""
	}

	fun getPreferencesData(): Flow<Preferences> = preferences.data


}