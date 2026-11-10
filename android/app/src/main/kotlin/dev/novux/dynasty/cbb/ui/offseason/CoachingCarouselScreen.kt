package dev.novux.dynasty.cbb.ui.offseason

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import dev.novux.dynasty.cbb.core.carousel.CarouselStatus
import dev.novux.dynasty.cbb.core.carousel.CoachingCarouselEngine
import dev.novux.dynasty.cbb.core.carousel.CoachingCarouselState
import dev.novux.dynasty.cbb.core.save.LoadedDynasty
import dev.novux.dynasty.cbb.data.carousel.CoachingCarouselRepository
import kotlinx.coroutines.launch

/** Coaching Carousel — Season 2+ gate before Offseason Week 1 ([planning/offseason.md]). */
@Composable
fun CoachingCarouselScreen(
    loaded: LoadedDynasty,
    repository: CoachingCarouselRepository,
    onComplete: () -> Unit,
) {
    var carousel by remember { mutableStateOf<CoachingCarouselState?>(null) }
    val scope = rememberCoroutineScope()
    val seasonIndex = loaded.metadata.seasonIndex

    androidx.compose.runtime.LaunchedEffect(loaded.dynastyId, seasonIndex) {
        carousel = repository.loadOrCreate(loaded.dynastyId, seasonIndex)
    }

    val state = carousel ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.carousel_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stringResource(R.string.carousel_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        when (state.status) {
            CarouselStatus.AwaitingBackgroundSim -> {
                Text(
                    text = stringResource(R.string.carousel_background_hint),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f),
                )
                Button(
                    onClick = {
                        scope.launch {
                            val next = CoachingCarouselEngine.runBackgroundSim(state)
                            repository.save(loaded.dynastyId, seasonIndex, next)
                            carousel = next
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.carousel_sim_league))
                }
            }
            CarouselStatus.AwaitingUserApprovals -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f),
                ) {
                    items(state.pendingApprovals, key = { it.id }) { card ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(card.headline, fontWeight = FontWeight.SemiBold)
                                Text(card.detail, style = MaterialTheme.typography.bodyMedium)
                                OutlinedButton(
                                    onClick = {
                                        scope.launch {
                                            val next = CoachingCarouselEngine.resolveApproval(state, card.id, false)
                                            repository.save(loaded.dynastyId, seasonIndex, next)
                                            carousel = next
                                            if (next.canEnterOffseasonWeekOne()) onComplete()
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text(stringResource(R.string.carousel_decline))
                                }
                                Button(
                                    onClick = {
                                        scope.launch {
                                            val next = CoachingCarouselEngine.resolveApproval(state, card.id, true)
                                            repository.save(loaded.dynastyId, seasonIndex, next)
                                            carousel = next
                                            if (next.canEnterOffseasonWeekOne()) onComplete()
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text(stringResource(R.string.carousel_accept))
                                }
                            }
                        }
                    }
                }
            }
            CarouselStatus.Complete, CarouselStatus.NotRequired -> {
                Text(
                    text = stringResource(R.string.carousel_complete),
                    modifier = Modifier.weight(1f),
                )
                Button(onClick = onComplete, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.carousel_continue_offseason))
                }
            }
        }
    }
}
