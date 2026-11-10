package dev.novux.dynasty.cbb.core.pack.import

import java.net.URI

object PackUrlPolicy {
    private val blockedSchemes = setOf("file", "ftp", "javascript", "data", "content")

    fun validate(url: String): PackImportError? {
        val uri = try {
            URI(url.trim())
        } catch (_: Exception) {
            return PackImportError.UrlPolicy("Invalid URL")
        }
        val scheme = uri.scheme?.lowercase()
        if (scheme in blockedSchemes) {
            return PackImportError.UrlPolicy("Scheme not allowed: $scheme")
        }
        if (scheme != "https") {
            return PackImportError.UrlPolicy("Pack URLs must use HTTPS")
        }
        val host = uri.host?.lowercase()?.trimEnd('.') ?: return PackImportError.UrlPolicy("Missing host")
        if (hostMatchesLiteralIp(host)) {
            return PackImportError.UrlPolicy("IP-literal hosts are not allowed")
        }
        if (host !in PackImportConstants.ALLOWED_URL_HOSTS) {
            return PackImportError.UrlPolicy("Host not allowlisted: $host")
        }
        val port = uri.port
        if (port != -1 && port != 443) {
            return PackImportError.UrlPolicy("Only default HTTPS port is allowed")
        }
        if (uri.userInfo != null) {
            return PackImportError.UrlPolicy("User info in URL is not supported")
        }
        return null
    }

    private fun hostMatchesLiteralIp(host: String): Boolean {
        if (host.matches(Regex("""\d{1,3}(\.\d{1,3}){3}"""))) return true
        return host.contains(':') // IPv6 literal
    }
}
