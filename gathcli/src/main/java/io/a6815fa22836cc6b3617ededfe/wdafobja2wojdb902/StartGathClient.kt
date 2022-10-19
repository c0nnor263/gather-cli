package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902

// TODO StartGathClient
// @ObfustringThis
// class StartGathClient(private val activity: AppCompatActivity) : LifecycleOwner,
// 	StartGathUserInterface {
// 	private val manager: GathManager = GathManager(
// 		AppStoragePreferencesRepositoryImpl(
// 			AppStoragePreferencesDataStore(
// 				activity
// 			)
// 		),
// 		UserStoragePreferencesRepositoryImpl(
// 			UserStoragePreferencesDataStore(
// 				activity
// 			)
// 		)
// 	)
//
// 	init {
// 		fetchCountryCode()
// 	}
//
// 	override suspend fun getGoogleAdvertisingId(): String? {
// 		return withContext(Dispatchers.IO) {
// 			try {
// 				AdvertisingIdClient.getAdvertisingIdInfo(activity).id.toString()
// 			} catch (e: IOException) {
// 				Log.e(
// 					"StartGathClient",
// 					"googleAdvertisingId IOException: $e"
// 				)
// 				Crashlog.record(e)
// 				null
// 			} catch (e: IllegalStateException) {
// 				Log.e("StartGathClient", "googleAdvertisingId IllegalStateException: ${e.message}")
// 				Crashlog.record(e)
// 				null
// 			} catch (e: GooglePlayServicesNotAvailableException) {
// 				Log.e(
// 					"StartGathClient",
// 					"googleAdvertisingId GooglePlayServicesNotAvailableException: ${e.message}"
// 				)
// 				Crashlog.record(e)
// 				null
// 			} catch (e: GooglePlayServicesRepairableException) {
// 				Log.e(
// 					"StartGathClient",
// 					"googleAdvertisingId GooglePlayServicesRepairableException: ${e.message}"
// 				)
// 				null
// 			} catch (e: Exception) {
// 				Log.e("StartGathClient", "googleAdvertisingId Exception: ${e.message}")
// 				Crashlog.record(e)
// 				null
// 			}
// 		}
// 	}
//
// 	override fun sendURL(url: String) {
// 		scope {
// 			manager.sendInitData(getGoogleAdvertisingId(), url)
// 		}
// 	}
//
// 	override fun setup(block: StartGathClientConfig.() -> Unit): StartGathClient {
// 		scope {
// 			StartGathClientConfig().apply(block).apply {
// 				manager.customDatabaseURL(databaseUrl)
// 			}
// 		}
// 		return this
// 	}
//
// 	override fun getLifecycle(): Lifecycle {
// 		return activity.lifecycle
// 	}
// }