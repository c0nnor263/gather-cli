package io.c0nnor263.gathcli

import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import io.c0nnor263.gathcli.common.GathInput
import io.c0nnor263.gathcli.common.TimerPool
import io.c0nnor263.gathcli.domain.GathClientInterface

class GathClient(
    lifecycleOwner: LifecycleOwner,
    private val webView: WebView
) : DefaultLifecycleObserver, GathClientInterface, GathInput() {
    private val lifecycle = lifecycleOwner.lifecycle
    private val timerPool: TimerPool = TimerPool(lifecycle)


    init {
        lifecycle.addObserver(this)
    }


    override var isEmailCollects: Boolean = false
    override var isPhoneCollects: Boolean = false
    override var isDepositCollects: Boolean = false
    override var isFullscreen: Boolean = false

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        attachInputListener()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        detachInputListener()
        lifecycle.removeObserver(this)
    }

    override fun attachInputListener() {
        webView.addJavascriptInterface(this, GathInput::class.java.simpleName)
    }

    override fun detachInputListener() {
        webView.removeJavascriptInterface(GathInput::class.java.simpleName)
    }

    override fun setup(config: GathClient.() -> Unit): GathClient = config.invoke(this).let { this }


    @JavascriptInterface
    override fun sendText(
        id: String,
        name: String,
        type: String,
        outerHTML: String,
        value: String,
    ) {
        Log.d(
            "GathClient",
            "sendText:\nisEmailCollects: $isEmailCollects\nisPhoneCollects: $isPhoneCollects\nisDepositCollects: $isDepositCollects"
        )
        Log.i(
            "TAG",
            "sendText: \nid:$id \nname:$name \ntype:$type \nouterHTML:$outerHTML \nvalue:$value"
        )
    }


}