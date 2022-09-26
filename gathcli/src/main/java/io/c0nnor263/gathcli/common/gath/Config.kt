package io.c0nnor263.gathcli.common.gath

import io.c0nnor263.gathcli.domain.ConfigInterface

class Config : ConfigInterface {
    override var isEmailCollect: Boolean = false
    override var isPhoneCollect: Boolean = false
    override var isDepositCollect: Boolean = false
    override var isFullscreen: Boolean = false
    override var databaseUrl: String = ""
}