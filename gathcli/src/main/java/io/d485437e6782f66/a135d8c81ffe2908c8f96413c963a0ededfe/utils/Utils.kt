package io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.utils

import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.GathClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal fun GathClient.scope(block: suspend CoroutineScope.() -> Unit) {
    this.lifecycleScope.launch {
        block()
    }
}

internal val Firebase.databaseEmptyOrNot: (url: String) -> FirebaseDatabase
    get() = { databaseUrl ->
        if (databaseUrl.isNotBlank()) database(databaseUrl) else database
    }