package io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.data.source.remote

import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.common.volley.DefaultVolleyStringRequest
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
