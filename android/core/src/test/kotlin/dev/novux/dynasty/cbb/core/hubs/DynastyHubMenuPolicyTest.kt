package dev.novux.dynasty.cbb.core.hubs

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.calendar.CalendarPosition
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DynastyHubMenuPolicyTest {
  @Test
  fun marchMayhemVisibleInPostseason() {
    val position = CalendarPosition(1, CalendarPhase.Postseason, 1)
    assertTrue(DynastyHubMenuPolicy.marchMayhemMenuVisible(position))
  }

  @Test
  fun marchMayhemHiddenInRegularSeason() {
    val position = CalendarPosition(1, CalendarPhase.RegularSeason, 10)
    assertFalse(DynastyHubMenuPolicy.marchMayhemMenuVisible(position))
  }
}
