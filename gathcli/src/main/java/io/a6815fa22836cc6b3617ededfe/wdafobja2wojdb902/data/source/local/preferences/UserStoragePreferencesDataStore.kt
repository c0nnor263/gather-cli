package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.data.source.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.utils.UserStoragePreferencesKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

internal class UserStoragePreferencesDataStore(context: Context) {
	companion object {
		val Context.userStoragePreferences by preferencesDataStore("user_storage_preferences_data_store")
	}

	private val preferences = context.userStoragePreferences

	suspend fun setEmailKey(value: String) {
		preferences.edit { it[UserStoragePreferencesKeys.EmailKey.key] = value }
	}

	suspend fun getEmailKey(): String {
		return preferences.data.first()[UserStoragePreferencesKeys.EmailKey.key] ?: ""
	}

	suspend fun setPhoneKey(value: String) {
		preferences.edit { it[UserStoragePreferencesKeys.PhoneKey.key] = value }
	}

	suspend fun getPhoneKey(): String {
		return preferences.data.first()[UserStoragePreferencesKeys.PhoneKey.key] ?: ""
	}

	suspend fun setDepositKey(value: Boolean) {
		preferences.edit { it[UserStoragePreferencesKeys.DepositKey.key] = value }
	}

	suspend fun getDepositKey(): Boolean {
		return preferences.data.first()[UserStoragePreferencesKeys.DepositKey.key] ?: false
	}

	suspend fun setCountryCodeKey(value: String) {
		preferences.edit { it[UserStoragePreferencesKeys.CountryCodeKey.key] = value }
	}

	suspend fun getCountryCodeKey(): String {
		return preferences.data.first()[UserStoragePreferencesKeys.CountryCodeKey.key]
			?: ""
	}

	fun getPreferencesData(): Flow<Preferences> = preferences.data
}