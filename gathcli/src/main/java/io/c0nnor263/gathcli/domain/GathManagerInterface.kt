package io.c0nnor263.gathcli.domain

internal interface GathManagerInterface {
    var emailCollectingEnabled: Boolean
    var phoneCollectingEnabled: Boolean
    var depositCollectingEnabled: Boolean
    var customDatabaseUrl: String
}