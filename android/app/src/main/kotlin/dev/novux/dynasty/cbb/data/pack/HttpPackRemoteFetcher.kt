package dev.novux.dynasty.cbb.data.pack

import dev.novux.dynasty.cbb.core.pack.import.PackImportConstants
import dev.novux.dynasty.cbb.core.pack.import.PackImportError
import dev.novux.dynasty.cbb.core.pack.import.PackUrlPolicy
import java.net.HttpURLConnection
import java.net.URL

/**
 * Fetches pack bytes over HTTPS with size cap — allowlist enforced in [PackUrlPolicy].
 */
class HttpPackRemoteFetcher {

    fun fetch(url: String): Result<String> {
        PackUrlPolicy.validate(url)?.let { error ->
            return Result.failure(PackImportFetchException(error))
        }
        try {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.instanceFollowRedirects = true
            connection.connectTimeout = 15_000
            connection.readTimeout = 30_000
            connection.requestMethod = "GET"
            try {
                connection.connect()
                val code = connection.responseCode
                if (code !in 200..299) {
                    return Result.failure(PackImportFetchException(PackImportError.RemoteFetch("HTTP $code")))
                }
                val length = connection.contentLengthLong
                if (length > PackImportConstants.MAX_REMOTE_BYTES) {
                    return Result.failure(
                        PackImportFetchException(PackImportError.PayloadTooLarge(length)),
                    )
                }
                val bytes = connection.inputStream.buffered().use { it.readBytes() }
                if (bytes.size.toLong() > PackImportConstants.MAX_REMOTE_BYTES) {
                    return Result.failure(
                        PackImportFetchException(PackImportError.PayloadTooLarge(bytes.size.toLong())),
                    )
                }
                return Result.success(String(bytes, Charsets.UTF_8))
            } finally {
                connection.disconnect()
            }
        } catch (e: PackImportFetchException) {
            return Result.failure(e)
        } catch (e: Exception) {
            return Result.failure(
                PackImportFetchException(PackImportError.RemoteFetch(e.message ?: "fetch failed")),
            )
        }
    }
}

class PackImportFetchException(val importError: PackImportError) : IllegalStateException(importError.toString())
