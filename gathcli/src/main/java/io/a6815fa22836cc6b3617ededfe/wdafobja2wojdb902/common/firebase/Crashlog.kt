package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.common.firebase

import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.domain.CrashlyticsProvider

object Crashlog : CrashlyticsProvider {
	private val crashlytics = FirebaseCrashlytics.getInstance()
	override fun record(message: String, exception: Exception) {
		crashlytics.recordException(RuntimeException("$message   |   ${exception.message}"))
		crashlytics.sendUnsentReports()
	}

	override fun record(exception: Exception) {
		crashlytics.recordException(exception)
		crashlytics.sendUnsentReports()
	}
}