package io.c0nnor263.gathcli.utils

import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.c0nnor263.gathcli.GathClient
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