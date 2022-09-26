package io.c0nnor263.gathcli.common.volley

import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import kotlin.random.Random

class DefaultVolleyStringRequest {
    companion object {
        fun get(url: String, listener: Response.Listener<String>) =
            StringRequest(Request.Method.GET, url, listener) {
                Log.e("GathClient", "Request country code error: \n$url\n$it")
            }.apply {
                retryPolicy = DefaultRetryPolicy(Random.nextInt(20000, 30000), 5, 1f)
            }
    }
}