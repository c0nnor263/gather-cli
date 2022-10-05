package io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.common.gath

import io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.domain.ConfigInterface

class Config : ConfigInterface {
    override var isEmailCollect: Boolean = false
    override var isPhoneCollect: Boolean = false
    override var isDepositCollect: Boolean = false
    override var isFullscreen: Boolean = false
    override var databaseUrl: String = ""
}