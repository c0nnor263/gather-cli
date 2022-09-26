package io.c0nnor263.gathcli.domain

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