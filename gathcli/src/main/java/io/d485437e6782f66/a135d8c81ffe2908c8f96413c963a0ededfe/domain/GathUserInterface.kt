package io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.domain

import android.webkit.WebView
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.GathClient
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.common.gath.Config

interface GathUserInterface {
    fun evaluateJavascript(view: WebView?)
    fun evaluateJavascript(view: WebView?, script: String)
    fun setup(block: Config.() -> Unit): GathClient
    fun start(): GathClient
}