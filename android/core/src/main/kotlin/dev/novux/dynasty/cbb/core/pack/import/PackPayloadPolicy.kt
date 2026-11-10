package dev.novux.dynasty.cbb.core.pack.import

import java.io.InputStream

/** Local and remote import size limits ([planning/game-design.md#legal-ux-ship-checklist]). */
object PackPayloadPolicy {
    fun validateUtf8Payload(payload: String): PackImportError? =
        validateByteSize(payload.toByteArray(Charsets.UTF_8).size.toLong())

    fun validateByteSize(bytes: Long): PackImportError? {
        if (bytes < 0) return null
        if (bytes > PackImportConstants.MAX_REMOTE_BYTES) {
            return PackImportError.PayloadTooLarge(bytes)
        }
        return null
    }

    /**
     * Reads up to [PackImportConstants.MAX_REMOTE_BYTES] + 1 bytes — rejects overflow without
     * loading unbounded bodies when Content-Length is absent.
     */
    fun readUtf8Capped(input: InputStream): Pair<String?, PackImportError?> {
        val max = PackImportConstants.MAX_REMOTE_BYTES
        val buffer = ByteArray((max + 1).toInt())
        var total = 0
        while (true) {
            val read = input.read(buffer, total, buffer.size - total)
            if (read <= 0) break
            total += read
            if (total > max) {
                return null to PackImportError.PayloadTooLarge(total.toLong())
            }
        }
        return String(buffer, 0, total, Charsets.UTF_8) to null
    }
}
