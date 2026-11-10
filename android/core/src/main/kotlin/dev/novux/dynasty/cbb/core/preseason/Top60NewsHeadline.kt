package dev.novux.dynasty.cbb.core.preseason

import dev.novux.dynasty.cbb.core.signingday.NationalRankRow

data class Top60NewsHeadline(
    val seasonIndex: Int,
    val headline: String,
    val top60: List<NationalRankRow>,
)
