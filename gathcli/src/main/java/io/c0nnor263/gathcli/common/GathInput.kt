package io.c0nnor263.gathcli.common

import android.webkit.JavascriptInterface

open class GathInput {
    @JavascriptInterface
    open fun sendText(
        id: String,
        name: String,
        type: String,
        outerHTML: String,
        value: String,
    ) {}
}