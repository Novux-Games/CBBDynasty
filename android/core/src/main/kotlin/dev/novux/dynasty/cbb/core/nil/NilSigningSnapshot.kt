package dev.novux.dynasty.cbb.core.nil

data class NilSigningSnapshot(
    val prospectId: String,
    val nilAskUsd: Int,
    val nilSignedUsd: Int,
    /** Collective pool at hard sign (cycle frozen base + any fundraising bump). */
    val nilPoolAnnualUsd: Long = 0L,
)

object NilSigningSnapshotEngine {
    fun forProspect(
        prospectId: String,
        nilAskUsd: Int,
        nilOfferUsd: Int,
        nilPoolAnnualUsd: Long = 0L,
    ): NilSigningSnapshot =
        NilSigningSnapshot(
            prospectId = prospectId,
            nilAskUsd = nilAskUsd,
            nilSignedUsd = nilOfferUsd.coerceAtLeast(0),
            nilPoolAnnualUsd = nilPoolAnnualUsd,
        )
}
