package io.c0nnor263.gathcli.domain

import android.webkit.WebView
import io.c0nnor263.gathcli.GathClient
import io.c0nnor263.gathcli.common.gath.Config

interface GathUserInterface {
    fun evaluateJavascript(view: WebView?)
    fun evaluateJavascript(view: WebView?, script: String)
    fun setup(block: Config.() -> Unit): GathClient
    fun start(): GathClient
}