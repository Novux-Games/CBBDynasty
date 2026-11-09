package dev.novux.dynasty.cbb.core.pack.import

import java.net.URI

object PackUrlPolicy {
    fun validate(url: String): PackImportError? {
        val uri = try {
            URI(url.trim())
        } catch (_: Exception) {
            return PackImportError.UrlPolicy("Invalid URL")
        }
        if (uri.scheme?.lowercase() != "https") {
            return PackImportError.UrlPolicy("Pack URLs must use HTTPS")
        }
        val host = uri.host?.lowercase()?.trimEnd('.') ?: return PackImportError.UrlPolicy("Missing host")
        if (host !in PackImportConstants.ALLOWED_URL_HOSTS) {
            return PackImportError.UrlPolicy("Host not allowlisted: $host")
        }
        if (uri.userInfo != null) {
            return PackImportError.UrlPolicy("User info in URL is not supported")
        }
        return null
    }
}
