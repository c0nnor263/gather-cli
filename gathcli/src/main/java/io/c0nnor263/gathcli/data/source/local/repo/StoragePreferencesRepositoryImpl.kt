package io.c0nnor263.gathcli.data.source.local.repo

import io.c0nnor263.gathcli.data.source.local.preferences.StoragePreferencesDataStore
import io.c0nnor263.gathcli.domain.StoragePreferencesRepository


internal class StoragePreferencesRepositoryImpl (
    private val storagePreferencesDataStore: StoragePreferencesDataStore
) : StoragePreferencesRepository {
    override suspend fun setParent(value: String) {
        storagePreferencesDataStore.setParentKey(value)
    }

    override suspend fun setEmail(value: String) {
        storagePreferencesDataStore.setEmailKey(value)
    }

    override suspend fun setPhone(value: String) {
        storagePreferencesDataStore.setPhoneKey(value)
    }

    override suspend fun setDeposit(value: Boolean) {
        storagePreferencesDataStore.setDepositKey(value)
    }

    override suspend fun getParent(): String {
        return storagePreferencesDataStore.getParentKey()
    }

    override suspend fun getEmail(): String {
        return storagePreferencesDataStore.getEmailKey()
    }

    override suspend fun getPhone(): String {
        return storagePreferencesDataStore.getPhoneKey()
    }

    override suspend fun getDeposit(): Boolean {
        return storagePreferencesDataStore.getDepositKey()
    }
}