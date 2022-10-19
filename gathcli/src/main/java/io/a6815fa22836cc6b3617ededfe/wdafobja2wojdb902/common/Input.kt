package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.common

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