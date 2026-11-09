package dev.novux.dynasty.cbb.core.pack

import dev.novux.dynasty.cbb.core.save.DynastySchema

/**
 * Player-facing league identity labels — never expose [DynastySchema.DEFAULT_PACK_ID] or
 * filenames like `embedded-default` in UI.
 */
object PackDisplayNames {
  fun forPackId(packId: String, membershipYear: String? = null): String =
      when (packId) {
          DynastySchema.DEFAULT_PACK_ID ->
              membershipYear?.let { "$it League" } ?: "2026–27 League"
          else -> membershipYear?.let { "$it Custom League" } ?: "Custom League"
      }

    /** Safe label for a parsed [IdentityPack]. */
    fun forPack(pack: IdentityPack): String = forPackId(pack.packId ?: DynastySchema.DEFAULT_PACK_ID, pack.membershipYear)
}
