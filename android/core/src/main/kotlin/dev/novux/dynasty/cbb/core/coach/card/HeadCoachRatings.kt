package dev.novux.dynasty.cbb.core.coach.card

data class HeadCoachRatings(
    val offensive: Int,
    val defensive: Int,
    val training: Int,
    val recruiting: Int,
) {
    init {
        require(offensive in 0..99)
        require(defensive in 0..99)
        require(training in 0..99)
        require(recruiting in 0..99)
    }
}
