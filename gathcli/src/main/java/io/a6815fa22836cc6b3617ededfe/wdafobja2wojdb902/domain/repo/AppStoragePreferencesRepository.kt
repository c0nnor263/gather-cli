package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.domain.repo

import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.domain.DataStorePreferencesInterface

internal interface AppStoragePreferencesRepository : DataStorePreferencesInterface {
	suspend fun setParent(value: String)
	suspend fun getParent(): String
	suspend fun setDatabaseURLKey(value: String)
	suspend fun getDatabaseURLKey(): String
	suspend fun setBuildURLKey(value: String)
	suspend fun getBuildURLKey(): String
}