package io.c0nnor263.gathcli.domain

import io.c0nnor263.gathcli.GathClient

interface GathClientInterface {
    fun attachInputListener()
    fun detachInputListener()
    fun setup(config: GathClient.() -> Unit): GathClient
    var isEmailCollects: Boolean
    var isPhoneCollects: Boolean
    var isDepositCollects: Boolean
    var isFullscreen: Boolean
}