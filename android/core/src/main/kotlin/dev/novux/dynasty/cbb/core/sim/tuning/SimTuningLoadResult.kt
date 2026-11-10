package dev.novux.dynasty.cbb.core.sim.tuning

sealed class SimTuningLoadResult {
    data class Success(val tuning: SimTuning) : SimTuningLoadResult()
    data class Rejected(val error: SimTuningValidationError) : SimTuningLoadResult()
}

sealed class SimTuningValidationError {
    data class MalformedJson(val message: String) : SimTuningValidationError()
    data class TuningIdMismatch(val expected: String, val actual: String) : SimTuningValidationError()
    data class SchemaVersionMismatch(val expected: Int, val actual: Int) : SimTuningValidationError()
    data class InvalidRange(val field: String, val detail: String) : SimTuningValidationError()
    data class MissingSection(val section: String) : SimTuningValidationError()
    data class UnknownTuningId(val id: String) : SimTuningValidationError()

    override fun toString(): String = when (this) {
        is MalformedJson -> "Malformed sim tuning JSON: $message"
        is TuningIdMismatch -> "simTuningId mismatch: expected $expected, got $actual"
        is SchemaVersionMismatch -> "schemaVersion mismatch: expected $expected, got $actual"
        is InvalidRange -> "Invalid $field: $detail"
        is MissingSection -> "Missing sim tuning section: $section"
        is UnknownTuningId -> "Unknown simTuningId: $id"
    }
}
