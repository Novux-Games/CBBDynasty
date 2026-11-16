package dev.novux.dynasty.cbb.core.selection

/** Flagship Selection Sunday reveal moment ([MEGA-POST-033], [MEGA-POST-145]). */
object SelectionSundayRevealPolicy {
    const val MEGA_ID: String = "MEGA-POST-033"
    const val FLAGSHIP_ID: String = "MEGA-POST-145"

    fun summarize(snapshot: SelectionSundaySnapshot): String =
        "NCBA ${snapshot.ncbaField.size} · NCI ${snapshot.nciField.size} · HC ${snapshot.heritageField.size}"
}