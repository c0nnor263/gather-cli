package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.domain

interface CrashlyticsProvider {
	fun record(message: String, exception: Exception)
	fun record(exception: Exception)
}