package dev.novux.dynasty.cbb.core.nil

data class NilSigningSnapshot(
    val prospectId: String,
    val nilAskUsd: Int,
    val nilSignedUsd: Int,
)

object NilSigningSnapshotEngine {
    fun forProspect(prospectId: String, nilAskUsd: Int, nilOfferUsd: Int): NilSigningSnapshot =
        NilSigningSnapshot(
            prospectId = prospectId,
            nilAskUsd = nilAskUsd,
            nilSignedUsd = nilOfferUsd.coerceAtLeast(0),
        )
}
