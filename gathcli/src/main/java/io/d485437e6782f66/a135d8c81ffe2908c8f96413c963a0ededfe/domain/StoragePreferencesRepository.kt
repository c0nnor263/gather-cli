package io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.domain

internal interface StoragePreferencesRepository {
    suspend fun setParent(value: String)
    suspend fun setEmail(value: String)
    suspend fun setPhone(value: String)
    suspend fun setDeposit(value: Boolean)
    suspend fun getParent(): String
    suspend fun getEmail(): String
    suspend fun getPhone(): String
    suspend fun getDeposit(): Boolean
}