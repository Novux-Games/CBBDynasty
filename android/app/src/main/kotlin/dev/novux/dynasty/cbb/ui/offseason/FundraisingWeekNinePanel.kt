package dev.novux.dynasty.cbb.ui.offseason

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.offseason.weeknine.FacilityCategory
import dev.novux.dynasty.cbb.core.offseason.weeknine.FacilityUpgradePolicy
import dev.novux.dynasty.cbb.core.offseason.weeknine.FundraisingEventType
import dev.novux.dynasty.cbb.core.offseason.weeknine.ProgramEconomyState
import dev.novux.dynasty.cbb.data.offseason.OffseasonWeekNineRepository
import dev.novux.dynasty.cbb.data.recruiting.RecruitingEconomyRepository
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun FundraisingWeekNinePanel(
    dynastyId: String,
    seasonIndex: Int,
    weekNineRepository: OffseasonWeekNineRepository,
    programPrestige: Int = RecruitingEconomyRepository.DEFAULT_PROGRAM_PRESTIGE,
    modifier: Modifier = Modifier,
) {
    var state by remember { mutableStateOf<ProgramEconomyState?>(null) }
    val scope = rememberCoroutineScope()
    val currency = remember { NumberFormat.getCurrencyInstance(Locale.US) }

    suspend fun refresh() {
        state = weekNineRepository.loadEconomy(dynastyId, seasonIndex)
    }

    LaunchedEffect(dynastyId, seasonIndex) {
        refresh()
    }

    val economy = state ?: return

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.fundraising_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = stringResource(
                R.string.fundraising_balances,
                currency.format(economy.cashUsd),
                currency.format(economy.nilBoostUsd),
            ),
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = stringResource(R.string.fundraising_events_heading),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        FundraisingEventType.entries.forEach { event ->
            val done = event in economy.completedEvents
            OutlinedButton(
                onClick = {
                    scope.launch {
                        weekNineRepository.runFundraising(dynastyId, seasonIndex, event, programPrestige)
                        refresh()
                    }
                },
                enabled = !done,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    if (done) {
                        stringResource(R.string.fundraising_event_done, event.label)
                    } else {
                        stringResource(R.string.fundraising_run_event, event.label)
                    },
                )
            }
        }
        OutlinedButton(
            onClick = {
                scope.launch {
                    weekNineRepository.sellNamingRights(dynastyId, seasonIndex)
                    refresh()
                }
            },
            enabled = !economy.namingRightsSold,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                if (economy.namingRightsSold) {
                    stringResource(R.string.fundraising_naming_sold)
                } else {
                    stringResource(R.string.fundraising_naming_rights)
                },
            )
        }
        Text(
            text = stringResource(R.string.fundraising_facilities_heading),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 8.dp),
        )
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            items(FacilityCategory.entries) { category ->
                val grade = economy.facilityGrades.grade(category)
                val cost = FacilityUpgradePolicy.upgradeCostUsd(grade)
                val canBuy = FacilityUpgradePolicy.canAfford(economy.cashUsd, economy.facilityGrades, category)
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column {
                            Text(category.label, fontWeight = FontWeight.Medium)
                            Text(stringResource(R.string.fundraising_facility_grade, grade))
                        }
                        OutlinedButton(
                            onClick = {
                                scope.launch {
                                    weekNineRepository.purchaseFacilityUpgrade(dynastyId, seasonIndex, category)
                                    refresh()
                                }
                            },
                            enabled = canBuy,
                        ) {
                            Text(stringResource(R.string.fundraising_upgrade_cost, currency.format(cost)))
                        }
                    }
                }
            }
        }
    }
}
