package io.c0nnor263.gathcli.common.gath

import android.webkit.JavascriptInterface

internal open class Input {

    @JavascriptInterface
    open fun sendText(
        id: String,
        name: String,
        type: String,
        outerHTML: String,
        value: String,
    ) {}
}