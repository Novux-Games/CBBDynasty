package dev.novux.dynasty.cbb.core.coach.card

data class AssistantCoachRatings(
    val recruiting: Int,
    val development: Int,
    val scouting: Int,
) {
    init {
        require(recruiting in 0..99)
        require(development in 0..99)
        require(scouting in 0..99)
    }
}
