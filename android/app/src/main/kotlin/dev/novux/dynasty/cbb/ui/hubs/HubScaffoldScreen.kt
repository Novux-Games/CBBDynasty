package dev.novux.dynasty.cbb.ui.hubs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HubScaffoldScreen(
    titleRes: Int,
    bodyRes: Int,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(title = { Text(stringResource(titleRes)) })
        OutlinedButton(onClick = onBack, modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(stringResource(R.string.dynasty_shell_back))
        }
        Text(
            text = stringResource(bodyRes),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            fontWeight = FontWeight.Normal,
        )
    }
}
