package dev.novux.dynasty.cbb.core.preseason

/**
 * Preseason task 1 seeds the 2k–4k HS/JUCO class manually; wizard year-one bootstrap pre-seeds before
 * exhibitions ([MEGA-RECR-052]).
 */
object PreseasonIncomingClassSeedTaskPolicy {
    const val MEGA_ID: String = "MEGA-RECR-052"

    val taskId: PreseasonTaskId = PreseasonTaskId.IncomingClassSeed
}