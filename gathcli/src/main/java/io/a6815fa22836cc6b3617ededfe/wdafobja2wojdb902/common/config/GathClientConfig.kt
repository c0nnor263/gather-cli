package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.common.config

import io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.domain.user.GathClientConfigInterface

class GathClientConfig : GathClientConfigInterface {
    override var isEmailCollect: Boolean = false
    override var isPhoneCollect: Boolean = false
    override var isDepositCollect: Boolean = false
    override var isFullscreen: Boolean = false
    override var databaseUrl: String = ""
}