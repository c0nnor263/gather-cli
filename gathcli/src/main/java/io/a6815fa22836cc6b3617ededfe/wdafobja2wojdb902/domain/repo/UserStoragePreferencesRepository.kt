package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.domain.repo

import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.domain.DataStorePreferencesInterface

internal interface UserStoragePreferencesRepository: DataStorePreferencesInterface {
    suspend fun setEmail(value: String)
    suspend fun getEmail(): String
    suspend fun setPhone(value: String)
    suspend fun getPhone(): String
    suspend fun setDeposit(value: Boolean)
    suspend fun getDeposit(): Boolean
    suspend fun setCountryCodeKey(value: String)
    suspend fun getCountryCodeKey(): String
}

