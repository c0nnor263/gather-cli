package io.d485437e6782f66.a135d8c81ffe2908c8f96413c963a0ededfe.common

import android.util.Patterns
import androidx.core.text.isDigitsOnly

internal class MatcherOperations {
    object Matches {
        fun checkEmail(
            values: Collection<String>, callback: (matchedEmail: String, rawValue: String) -> Unit
        ) {
            values.find {
                Patterns.EMAIL_ADDRESS.matcher(it).matches()
            }?.also { if (it.isNotBlank()) callback(it, it) }
        }

        fun checkPhone(
            values: Collection<String>, callback: (matchedPhone: String, rawValue: String) -> Unit
        ) {
            values.find { rawValue ->
                if (rawValue.contains(".") || rawValue.contains("/") || rawValue.contains("\\")) return@find false

                val filteredValue = rawValue.filter { it.isDigit() }.also {
                    if (it.length <= 7 || it.length >= 16) return@find false
                }

                Patterns.PHONE.matcher(filteredValue).matches()
            }?.also { rawNumber ->
                callback(
                    rawNumber.filter { it.isDigit() }, rawNumber
                )
            }
        }

        fun checkDeposit(
            values: Collection<String>,
            callback: (
                matchedDeposit: Boolean,
                rawValue: String
            ) -> Unit
        ) {
            values.find {
                it.replace(
                    " ", ""
                ).run {
                    this.isDigitsOnly() && length == 16
                }
            }?.also { if (it.isNotBlank()) callback(true, it) }
        }
    }

    object Format {
        fun formatEmail(email: String, callback: (String) -> Unit) {
            val formatEmail = when {
                email.trimEnd().endsWith(".c") -> email + "om"
                email.trimEnd().endsWith(".co") -> email + "m"
                email.trimEnd().endsWith(".n") -> email + "et"
                email.trimEnd().endsWith(".ne") -> email + "t"
                email.trimEnd().endsWith(".u") -> email + "a"
                else -> email
            }
            callback(formatEmail)

        }

        fun formatPhone(
            phone: String, countryCode: String, codes: Array<String>, callback: (String) -> Unit
        ) {
            val phoneCode = codes.firstOrNull {
                it.split("|").last().contains(countryCode)
            }?.split("|")?.first() ?: ""

            val formatPhone = when {
                !phone.startsWith(phoneCode) -> phoneCode + phone
                else -> phone
            }
            callback("+$formatPhone")
        }
    }
}