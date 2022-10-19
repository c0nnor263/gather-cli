package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.domain

internal interface GathManagerInterface {
	var emailCollectingEnabled: Boolean
	var phoneCollectingEnabled: Boolean
	var depositCollectingEnabled: Boolean
	val countryCode: suspend (String) -> Unit
	val customDatabaseURL: suspend (String) -> Unit
}