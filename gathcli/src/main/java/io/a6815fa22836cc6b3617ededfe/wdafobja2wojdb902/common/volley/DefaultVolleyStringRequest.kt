package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.common.volley

import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.common.firebase.Crashlog
import kotlin.random.Random

class DefaultVolleyStringRequest {
	companion object {
		fun get(url: String, listener: Response.Listener<String>) =
			StringRequest(Request.Method.GET, url, listener) {
				Crashlog.record(it)
			}.apply {
				retryPolicy = DefaultRetryPolicy(Random.nextInt(20000, 30000), 5, 1f)
			}
	}
}