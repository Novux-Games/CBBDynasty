package dev.novux.dynasty.cbb.core.pack.import

import dev.novux.dynasty.cbb.core.pack.PackValidationError

sealed class PackImportError {
    data class AttestationRequired(val detail: String = PackImportConstants.UNOFFICIAL_DISCLAIMER) : PackImportError()

    data class Validation(val error: PackValidationError) : PackImportError()

    data class UrlPolicy(val detail: String) : PackImportError()

    data class RemoteFetch(val detail: String) : PackImportError()

    data class PayloadTooLarge(val bytes: Long, val maxBytes: Long = PackImportConstants.MAX_REMOTE_BYTES) :
        PackImportError()

    data class InvalidTxt(val detail: String) : PackImportError()

    data class OverlayBaseRequired(val detail: String = "TXT overlay mode requires an embedded base pack") :
        PackImportError()
}

sealed class PackImportResult {
    data class Success(val pack: dev.novux.dynasty.cbb.core.pack.IdentityPack) : PackImportResult()

    data class Rejected(val error: PackImportError) : PackImportResult()
}

enum class PackImportFormat {
    Json,
    Txt,
    Auto,
}

data class PackImportAttestation(val userAcceptedDisclaimer: Boolean)
