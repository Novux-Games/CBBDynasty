package dev.novux.dynasty.cbb.core.sim.tuning

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

object SimTuningCodec {
    private val json = Json {
        ignoreUnknownKeys = false
        isLenient = false
        explicitNulls = false
    }

    fun parseAndValidate(
        raw: String,
        expectedTuningId: String? = null,
        expectedSchemaVersion: Int? = null,
    ): SimTuningLoadResult {
        val decoded = try {
            json.decodeFromString<SimTuning>(raw)
        } catch (e: SerializationException) {
            return SimTuningLoadResult.Rejected(
                SimTuningValidationError.MalformedJson(e.message ?: "decode failed"),
            )
        } catch (e: IllegalArgumentException) {
            return SimTuningLoadResult.Rejected(
                SimTuningValidationError.MalformedJson(e.message ?: "decode failed"),
            )
        }

        val error = SimTuningValidator.validate(decoded, expectedTuningId, expectedSchemaVersion)
        return if (error != null) {
            SimTuningLoadResult.Rejected(error)
        } else {
            SimTuningLoadResult.Success(decoded)
        }
    }
}
