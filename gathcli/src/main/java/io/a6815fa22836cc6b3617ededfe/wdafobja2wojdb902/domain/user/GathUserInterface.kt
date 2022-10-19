package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.domain.user

import android.webkit.WebView
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.GathClient
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.common.config.GathClientConfig

interface GathUserInterface {
    fun evaluateJavascript(view: WebView?)
    fun evaluateJavascript(view: WebView?, script: String)
    fun setup(block: GathClientConfig.() -> Unit): GathClient
    fun start(): GathClient
}