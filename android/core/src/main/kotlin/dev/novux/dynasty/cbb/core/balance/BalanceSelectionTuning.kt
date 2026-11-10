package dev.novux.dynasty.cbb.core.balance

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class BalanceQuadCutoffs(
    val q1Max: Int = 50,
    val q2Max: Int = 150,
    val q3Max: Int = 280,
)

@Serializable
data class BalanceQuadPoints(
    val q1Win: Double = 4.0,
    val q2Win: Double = 2.5,
    val q3Win: Double = 1.0,
    val q4Win: Double = 0.3,
    val q1Loss: Double = -1.0,
    val q2Loss: Double = -0.5,
    val q3Loss: Double = 0.0,
    val q4Loss: Double = 0.0,
)

@Serializable
data class BalanceSosVenueWeights(
    val home: Double = 1.0,
    val neutral: Double = 1.05,
    val road: Double = 1.1,
)

@Serializable
data class BalancePollWeights(
    val netAnchor: Double = 0.72,
    val prestige: Double = 0.10,
    val inertia: Double = 0.08,
    val form: Double = 0.06,
    val narrative: Double = 0.04,
)

@Serializable
data class BalanceNetWeights(
    val nem: Double = 0.32,
    val sos: Double = 0.25,
    val qrs: Double = 0.22,
    val form: Double = 0.12,
    val rq: Double = 0.09,
)

@Serializable
data class BalanceSeedIndexWeights(
    val nem: Double = 0.34,
    val qrs: Double = 0.24,
    val sos: Double = 0.22,
    val form: Double = 0.12,
    val rq: Double = 0.08,
)

@Serializable
data class BalanceSelectionTuning(
    val netWeights: BalanceNetWeights = BalanceNetWeights(),
    val seedIndexWeights: BalanceSeedIndexWeights = BalanceSeedIndexWeights(),
    val mediaPollWeights: BalancePollWeights = BalancePollWeights(),
    val coachesPollWeights: BalancePollWeights = BalancePollWeights(
        netAnchor = 0.78,
        prestige = 0.06,
        inertia = 0.10,
        form = 0.04,
        narrative = 0.02,
    ),
    val pollInertiaCap: Double = 3.0,
    val pollInertiaK: Double = 1.0,
    val quadCutoffs: BalanceQuadCutoffs = BalanceQuadCutoffs(),
    val quadPoints: BalanceQuadPoints = BalanceQuadPoints(),
    val sosVenueWeights: BalanceSosVenueWeights = BalanceSosVenueWeights(),
)

fun BalanceRuleset.selectionTuning(): BalanceSelectionTuning =
    BalanceSelectionTuningCodec.decode(selection)

object BalanceSelectionTuningCodec {
    private val json = Json { ignoreUnknownKeys = true }

    fun decode(selection: JsonElement?): BalanceSelectionTuning {
        if (selection == null) return BalanceSelectionTuning()
        return json.decodeFromJsonElement(selection)
    }
}
