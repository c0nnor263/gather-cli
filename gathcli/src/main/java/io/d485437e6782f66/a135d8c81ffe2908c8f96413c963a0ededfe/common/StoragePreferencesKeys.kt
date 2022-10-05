package io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.common

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

internal sealed class StoragePreferencesKeys<T>(val key: Preferences.Key<T>) {
        object ParentKey : StoragePreferencesKeys<String>(stringPreferencesKey("parent_key"))
        object EmailKey : StoragePreferencesKeys<String>(stringPreferencesKey("email_key"))
        object PhoneKey : StoragePreferencesKeys<String>(stringPreferencesKey("phone_key"))
        object DepositKey : StoragePreferencesKeys<Boolean>(booleanPreferencesKey("deposit_key"))
    }