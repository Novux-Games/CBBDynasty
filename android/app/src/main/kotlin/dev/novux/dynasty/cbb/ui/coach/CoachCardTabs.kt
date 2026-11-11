package dev.novux.dynasty.cbb.ui.coach

enum class CoachCardTab(val wire: String) {
    Overview("overview"),
    Contract("contract"),
    History("history"),
    HallOfFame("hof"),
    Draft("draft"),
    ;

    companion object {
        val HEAD_COACH_ORDER = listOf(Overview, Contract, History, HallOfFame, Draft)
    }
}
