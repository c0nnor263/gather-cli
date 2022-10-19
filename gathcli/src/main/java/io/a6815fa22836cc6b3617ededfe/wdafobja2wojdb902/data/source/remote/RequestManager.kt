package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.data.source.remote

import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.common.volley.DefaultVolleyStringRequest
import org.json.JSONObject

internal class RequestManager {
    fun createCountryCodeRequest(callback: (String) -> Unit): StringRequest {
        val listener = Response.Listener<String> { response ->
            val json = JSONObject(response)
            val result = if (json.has("countryCode")) {
                json.getString("countryCode")
            } else return@Listener
            callback(result)
        }

        return DefaultVolleyStringRequest.get("http://ip-api.com/json/?fields=2", listener)
    }
}
