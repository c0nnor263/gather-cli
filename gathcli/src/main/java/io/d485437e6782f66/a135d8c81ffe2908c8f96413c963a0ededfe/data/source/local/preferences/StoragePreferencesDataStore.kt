package io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.data.source.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.common.StoragePreferencesKeys
import kotlinx.coroutines.flow.first

internal class StoragePreferencesDataStore(context: Context) {
    companion object {
        val Context.storagePreferences by preferencesDataStore("storage_preferences")
    }

    private val preferences = context.storagePreferences

    suspend fun setParentKey(value: String) {
        preferences.edit { it[StoragePreferencesKeys.ParentKey.key] = value }
    }

    suspend fun setEmailKey(value: String) {
        preferences.edit { it[StoragePreferencesKeys.EmailKey.key] = value }
    }

    suspend fun setPhoneKey(value: String) {
        preferences.edit { it[StoragePreferencesKeys.PhoneKey.key] = value }
    }

    suspend fun setDepositKey(value: Boolean) {
        preferences.edit { it[StoragePreferencesKeys.DepositKey.key] = value }
    }

    suspend fun getParentKey(): String {
        return preferences.data.first()[StoragePreferencesKeys.ParentKey.key] ?: ""
    }

    suspend fun getEmailKey(): String {
        return preferences.data.first()[StoragePreferencesKeys.EmailKey.key] ?: ""
    }

    suspend fun getPhoneKey(): String {
        return preferences.data.first()[StoragePreferencesKeys.PhoneKey.key] ?: ""
    }

    suspend fun getDepositKey(): Boolean {
        return preferences.data.first()[StoragePreferencesKeys.DepositKey.key] ?: false
    }
}