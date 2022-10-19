package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

internal sealed class UserStoragePreferencesKeys {
		object EmailKey : AppStoragePreferencesKeys<String>(stringPreferencesKey("email_key"))
		object PhoneKey : AppStoragePreferencesKeys<String>(stringPreferencesKey("phone_key"))
		object DepositKey : AppStoragePreferencesKeys<Boolean>(booleanPreferencesKey("deposit_key"))
		object CountryCodeKey : AppStoragePreferencesKeys<String>(stringPreferencesKey("country_code_key"))
	}