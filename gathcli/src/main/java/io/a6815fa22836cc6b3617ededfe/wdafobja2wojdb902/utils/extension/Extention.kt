package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.utils.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.common.firebase.Crashlog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


internal val Firebase.databaseEmptyOrNot: (url: String) -> FirebaseDatabase
	get() = { databaseUrl ->
		if (databaseUrl.isNotBlank()) database(databaseUrl) else database
	}

internal fun LifecycleOwner.scope(block: suspend CoroutineScope.() -> Unit) {
	lifecycleScope.launch {
		try {
			block()
		} catch (e: Throwable) {
			Crashlog.record(RuntimeException(e))
		}
	}
}
