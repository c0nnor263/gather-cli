package io.c0nnor263.gathcli.data.source.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.c0nnor263.gathcli.common.StoragePreferencesKeys
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