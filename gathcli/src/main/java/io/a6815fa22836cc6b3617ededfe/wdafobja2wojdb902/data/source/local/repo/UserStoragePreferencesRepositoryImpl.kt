package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.data.source.local.repo

import android.util.Log
import androidx.datastore.preferences.core.Preferences
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.data.source.local.preferences.UserStoragePreferencesDataStore
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.domain.repo.UserStoragePreferencesRepository
import kotlinx.coroutines.flow.Flow

internal class UserStoragePreferencesRepositoryImpl(
	private val userStoragePreferencesDataStore: UserStoragePreferencesDataStore
) : UserStoragePreferencesRepository {

	override fun getPreferencesData(): Flow<Preferences> {
		return userStoragePreferencesDataStore.getPreferencesData()
	}

	override suspend fun setEmail(value: String) {
		getEmail().ifBlank {
			userStoragePreferencesDataStore.setEmailKey(value)
		}
	}

	override suspend fun getEmail(): String {
		return userStoragePreferencesDataStore.getEmailKey()
	}

	override suspend fun setPhone(value: String) {
		getPhone().ifBlank {
			userStoragePreferencesDataStore.setPhoneKey(value)
		}

	}

	override suspend fun getPhone(): String {
		return userStoragePreferencesDataStore.getPhoneKey()
	}

	override suspend fun setDeposit(value: Boolean) {
		if (!getDeposit()) {
			userStoragePreferencesDataStore.setDepositKey(value)
		}
	}

	override suspend fun getDeposit(): Boolean {
		return userStoragePreferencesDataStore.getDepositKey()
	}

	override suspend fun setCountryCodeKey(value: String) {
		getCountryCodeKey().ifBlank {
			userStoragePreferencesDataStore.setCountryCodeKey(value)
			Log.i("GathManager", "countryCode: $value")
		}

	}

	override suspend fun getCountryCodeKey(): String {
		return userStoragePreferencesDataStore.getCountryCodeKey()
	}


}