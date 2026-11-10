package dev.novux.dynasty.cbb.ui.hubs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.school.SchoolHubOverview
import dev.novux.dynasty.cbb.data.chemistry.ChemistryRepository
import dev.novux.dynasty.cbb.data.school.SchoolHubRepository
import dev.novux.dynasty.cbb.ui.school.SchoolHubScreen

@Composable
fun SchoolHubRouteScreen(
    dynastyId: String,
    seasonIndex: Int,
    schoolHubRepository: SchoolHubRepository,
    chemistryRepository: ChemistryRepository,
    onBack: () -> Unit,
) {
    var overview by remember { mutableStateOf<SchoolHubOverview?>(null) }
    var teamChem by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(dynastyId, seasonIndex) {
        overview = schoolHubRepository.loadUserSchoolOverview(dynastyId, seasonIndex)
        teamChem = chemistryRepository.loadState(dynastyId, seasonIndex).teamChemistry
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedButton(onClick = onBack, modifier = Modifier.padding(16.dp)) {
            Text(stringResource(R.string.dynasty_shell_back))
        }
        when (val school = overview) {
            null -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            else -> SchoolHubScreen(overview = school, teamChemistry = teamChem)
        }
    }
}
