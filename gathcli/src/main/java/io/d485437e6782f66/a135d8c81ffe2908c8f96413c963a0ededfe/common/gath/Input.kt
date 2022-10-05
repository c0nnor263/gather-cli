package io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.common.gath

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