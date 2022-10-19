package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.domain

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

internal interface DataStorePreferencesInterface{
    fun getPreferencesData(): Flow<Preferences>
}