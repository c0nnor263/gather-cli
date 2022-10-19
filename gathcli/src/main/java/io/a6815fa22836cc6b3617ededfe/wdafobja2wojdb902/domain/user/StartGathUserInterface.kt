package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.domain.user

interface StartGathUserInterface {
	suspend fun getGoogleAdvertisingId(): String?
	fun sendURL(url: String)
	// TODO add more methods
	//fun setup(block: StartGathClientConfig.() -> Unit): StartGathClient
}