package dev.novux.dynasty.cbb.core.recruiting

/**
 * Incoming class seed must not block the UI thread ([MEGA-RECR-131]).
 * Implementation: `IncomingClassSeedRepository` — IO for DataStore; Default for seed/encode/decode.
 */
object IncomingClassSeedIoPolicy {
    const val QA_ID: String = "MEGA-RECR-131"

    const val IO_DISPATCHER: String = "Dispatchers.IO"

    const val CPU_DISPATCHER: String = "Dispatchers.Default"

    const val REPOSITORY_CLASS: String =
        "dev.novux.dynasty.cbb.data.preseason.IncomingClassSeedRepository"

    val CPU_BOUND_OPERATIONS: List<String> = listOf(
        "IncomingClassSeeder.seed",
        "encodePool",
        "decodePool",
        "Top60NewsPublisher.publish",
    )
}
