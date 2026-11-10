package dev.novux.dynasty.cbb.core.player.scout

enum class ScoutDepth(val fuzzSteps: Int) {
    Unscouted(2),
    Light(1),
    Full(0),
}
