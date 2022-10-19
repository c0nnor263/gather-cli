package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.data.source.local.repo

import android.util.Log
import androidx.datastore.preferences.core.Preferences
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.data.source.local.preferences.AppStoragePreferencesDataStore
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.domain.repo.AppStoragePreferencesRepository
import kotlinx.coroutines.flow.Flow


internal class AppStoragePreferencesRepositoryImpl(
	private val appStoragePreferencesDataStore: AppStoragePreferencesDataStore
) : AppStoragePreferencesRepository {
	override fun getPreferencesData(): Flow<Preferences> {
		return appStoragePreferencesDataStore.getPreferencesData()
	}

	override suspend fun setParent(value: String) {
		getParent().ifBlank {
			appStoragePreferencesDataStore.setParentKey(value)
		}
	}

	override suspend fun getParent(): String {
		return appStoragePreferencesDataStore.getParentKey()
	}

	override suspend fun setDatabaseURLKey(value: String) {
		if(value.isNotBlank()) {
			appStoragePreferencesDataStore.setDatabaseURLKey(value)
			Log.i("GathManager", "setDatabaseURLKey: $value")
		}

	}

	override suspend fun getDatabaseURLKey(): String {
		return appStoragePreferencesDataStore.getDatabaseURLKey()
	}

	override suspend fun setBuildURLKey(value: String) {
		appStoragePreferencesDataStore.setBuildURLKey(value)
		Log.i("TAG", "setBuildURLKey: $value")
	}

	override suspend fun getBuildURLKey(): String {
		return appStoragePreferencesDataStore.getBuildURLKey()
	}
}