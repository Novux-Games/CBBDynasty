package dev.novux.dynasty.cbb.data.pack

import android.content.Context
import dev.novux.dynasty.cbb.core.pack.IdentityPackCodec
import dev.novux.dynasty.cbb.core.pack.PackLoadResult
import dev.novux.dynasty.cbb.core.pack.PackOverlaySession
import dev.novux.dynasty.cbb.core.pack.PackOverlaySessions
import dev.novux.dynasty.cbb.core.pack.PackSwapResult
import dev.novux.dynasty.cbb.core.pack.TeamDisplayOverlay
import dev.novux.dynasty.cbb.core.save.DynastySchema
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Loads and holds the active identity pack overlay — display strings only.
 * Dynasty SQLite rows reference `teamId` / `confId`; swapping packs rebuilds this cache.
 */
class PackRepository(
    private val context: Context,
) {
    private val mutex = Mutex()
    private var session: PackOverlaySession? = null

    suspend fun ensureEmbeddedLoaded(): PackOverlaySession = mutex.withLock {
        session?.let { return it }
        val loaded = loadAsset(DynastySchema.EMBEDDED_PACK_ASSET)
        session = loaded
        loaded
    }

    suspend fun activeSession(): PackOverlaySession = ensureEmbeddedLoaded()

    suspend fun teamDisplay(teamId: String): TeamDisplayOverlay? =
        activeSession().teamDisplay(teamId)

    suspend fun swapFromJson(raw: String): Result<PackSwapResult> = mutex.withLock {
        when (val parsed = IdentityPackCodec.parseAndValidate(raw)) {
            is PackLoadResult.Rejected ->
                Result.failure(PackParseException(parsed.error.toString()))
            is PackLoadResult.Success -> {
                val current = session ?: PackOverlaySessions.fromValidatedPack(parsed.pack)
                val swap = PackOverlaySessions.swap(current, parsed.pack)
                session = swap.session
                Result.success(swap)
            }
        }
    }

    suspend fun restorePack(packId: String): PackOverlaySession = mutex.withLock {
        if (packId == DynastySchema.DEFAULT_PACK_ID) {
            val loaded = loadAsset(DynastySchema.EMBEDDED_PACK_ASSET)
            session = loaded
            return loaded
        }
        val existing = session
        if (existing != null && existing.packId == packId) return existing
        error("Unknown pack id: $packId")
    }

    private fun loadAsset(assetPath: String): PackOverlaySession {
        val raw = context.assets.open(assetPath).bufferedReader().use { it.readText() }
        return when (val result = IdentityPackCodec.parseAndValidate(raw)) {
            is PackLoadResult.Success -> PackOverlaySessions.fromValidatedPack(result.pack)
            is PackLoadResult.Rejected -> throw PackParseException(result.error.toString())
        }
    }
}

class PackParseException(message: String) : IllegalStateException(message)
