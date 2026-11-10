package dev.novux.dynasty.cbb.core.schedule

import kotlinx.serialization.Serializable

@Serializable
enum class ScheduleGameKind {
    Exhibition,
    Conference,
    NonConference,
    EarlyEvent,
}
