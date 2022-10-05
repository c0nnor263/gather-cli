package io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.domain

interface ConfigInterface {
    var isEmailCollect: Boolean
    var isPhoneCollect: Boolean
    var isDepositCollect: Boolean

    /**
    Temporarily unavailable
     */
    var isFullscreen: Boolean

    var databaseUrl: String
}