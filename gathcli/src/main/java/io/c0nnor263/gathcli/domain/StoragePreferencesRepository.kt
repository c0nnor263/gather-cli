package io.c0nnor263.gathcli.domain

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