package io.a6815fa22836cc6b3617ededfe.wdafobja2wojdb902.domain.user

interface GathClientConfigInterface {
    var isEmailCollect: Boolean
    var isPhoneCollect: Boolean
    var isDepositCollect: Boolean

    /**
    Temporarily unavailable
     */
    var isFullscreen: Boolean

    var databaseUrl: String
}