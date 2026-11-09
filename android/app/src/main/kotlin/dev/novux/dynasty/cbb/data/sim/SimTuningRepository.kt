package dev.novux.dynasty.cbb.data.sim

import android.content.Context
import dev.novux.dynasty.cbb.core.save.DynastySchema
import dev.novux.dynasty.cbb.core.sim.tuning.SimTuning
import dev.novux.dynasty.cbb.core.sim.tuning.SimTuningCodec
import dev.novux.dynasty.cbb.core.sim.tuning.SimTuningLoadResult
import dev.novux.dynasty.cbb.core.sim.tuning.SimTuningValidationError
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Loads and holds the active sim tuning bundle for the session — pinned per dynasty.
 * See [docs/ADR-003-sim-tuning-bundle.md].
 */
class SimTuningRepository(
    private val context: Context,
) {
    private val mutex = Mutex()
    private var active: SimTuning? = null

    suspend fun ensureShippedDefaultLoaded(): SimTuning = mutex.withLock {
        active?.let { return it }
        loadAsset(DynastySchema.EMBEDDED_SIM_TUNING_ASSET).also { active = it }
    }

    suspend fun activeTuning(): SimTuning = ensureShippedDefaultLoaded()

    suspend fun restorePinned(tuningId: String, schemaVersion: Int): SimTuning = mutex.withLock {
        if (tuningId == DynastySchema.DEFAULT_SIM_TUNING_ID) {
            val tuning = loadAsset(
                DynastySchema.EMBEDDED_SIM_TUNING_ASSET,
                expectedTuningId = tuningId,
                expectedSchemaVersion = schemaVersion,
            )
            active = tuning
            return tuning
        }
        val assetPath = "sim/$tuningId.json"
        return try {
            val tuning = loadAsset(
                assetPath,
                expectedTuningId = tuningId,
                expectedSchemaVersion = schemaVersion,
            )
            active = tuning
            tuning
        } catch (_: SimTuningParseException) {
            throw SimTuningParseException(SimTuningValidationError.UnknownTuningId(tuningId).toString())
        }
    }

    private fun loadAsset(
        assetPath: String,
        expectedTuningId: String? = null,
        expectedSchemaVersion: Int? = null,
    ): SimTuning {
        val raw = context.assets.open(assetPath).bufferedReader().use { it.readText() }
        return when (
            val result = SimTuningCodec.parseAndValidate(
                raw,
                expectedTuningId,
                expectedSchemaVersion,
            )
        ) {
            is SimTuningLoadResult.Success -> result.tuning
            is SimTuningLoadResult.Rejected -> throw SimTuningParseException(result.error.toString())
        }
    }
}

class SimTuningParseException(message: String) : IllegalStateException(message)
