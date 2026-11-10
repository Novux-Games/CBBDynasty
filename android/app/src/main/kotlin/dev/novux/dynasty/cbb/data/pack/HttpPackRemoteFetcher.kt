package dev.novux.dynasty.cbb.data.pack

import dev.novux.dynasty.cbb.core.pack.import.PackImportError
import dev.novux.dynasty.cbb.core.pack.import.PackPayloadPolicy
import dev.novux.dynasty.cbb.core.pack.import.PackUrlPolicy
import java.net.HttpURLConnection
import java.net.URI
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
            var currentUrl = url
            var redirects = 0
            while (redirects <= MAX_REDIRECTS) {
                PackUrlPolicy.validate(currentUrl)?.let { error ->
                    return Result.failure(PackImportFetchException(error))
                }
                val connection = URL(currentUrl).openConnection() as HttpURLConnection
                connection.instanceFollowRedirects = false
                connection.connectTimeout = 15_000
                connection.readTimeout = 30_000
                connection.requestMethod = "GET"
                try {
                    connection.connect()
                    when (val code = connection.responseCode) {
                        in REDIRECT_RANGE -> {
                            if (redirects == MAX_REDIRECTS) {
                                return Result.failure(
                                    PackImportFetchException(PackImportError.RemoteFetch("Too many redirects")),
                                )
                            }
                            val location = connection.getHeaderField("Location")
                                ?: return Result.failure(
                                    PackImportFetchException(PackImportError.RemoteFetch("Redirect without Location")),
                                )
                            currentUrl = URI(currentUrl).resolve(location).toString()
                            redirects++
                        }
                        !in SUCCESS_RANGE -> {
                            return Result.failure(
                                PackImportFetchException(PackImportError.RemoteFetch("HTTP $code")),
                            )
                        }
                        else -> {
                            PackPayloadPolicy.validateByteSize(connection.contentLengthLong)?.let { error ->
                                return Result.failure(PackImportFetchException(error))
                            }
                            val (body, readError) = PackPayloadPolicy.readUtf8Capped(connection.inputStream)
                            if (readError != null) {
                                return Result.failure(PackImportFetchException(readError))
                            }
                            return Result.success(body!!)
                        }
                    }
                } finally {
                    connection.disconnect()
                }
            }
            return Result.failure(
                PackImportFetchException(PackImportError.RemoteFetch("Too many redirects")),
            )
        } catch (e: PackImportFetchException) {
            return Result.failure(e)
        } catch (e: Exception) {
            return Result.failure(
                PackImportFetchException(PackImportError.RemoteFetch(e.message ?: "fetch failed")),
            )
        }
    }

    private companion object {
        const val MAX_REDIRECTS = 5
        val REDIRECT_RANGE = 300..399
        val SUCCESS_RANGE = 200..299
    }
}

class PackImportFetchException(val importError: PackImportError) : IllegalStateException(importError.toString())
