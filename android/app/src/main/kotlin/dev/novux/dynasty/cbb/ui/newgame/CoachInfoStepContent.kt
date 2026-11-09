package dev.novux.dynasty.cbb.ui.newgame

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.coach.CoachAvatarPresets
import dev.novux.dynasty.cbb.core.coach.CoachCreateProfile
import dev.novux.dynasty.cbb.core.coach.CoachCreateValidationError
import dev.novux.dynasty.cbb.core.coach.CoachCreateValidator
import dev.novux.dynasty.cbb.core.coach.CoachNameSuffix
import dev.novux.dynasty.cbb.core.pack.TeamDisplayOverlay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CoachInfoStepContent(
    profile: CoachCreateProfile,
    schools: List<TeamDisplayOverlay>,
    onProfileChange: (CoachCreateProfile) -> Unit,
    showValidationErrors: Boolean,
    modifier: Modifier = Modifier,
) {
    val validationError = if (showValidationErrors) CoachCreateValidator.validate(profile) else null

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        OutlinedTextField(
            value = profile.firstName,
            onValueChange = { onProfileChange(profile.copy(firstName = it)) },
            label = { Text(stringResource(R.string.new_coach_first_name)) },
            isError = validationError is CoachCreateValidationError.FirstNameRequired,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        OutlinedTextField(
            value = profile.lastName,
            onValueChange = { onProfileChange(profile.copy(lastName = it)) },
            label = { Text(stringResource(R.string.new_coach_last_name)) },
            isError = validationError is CoachCreateValidationError.LastNameRequired,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        SuffixDropdown(
            selected = profile.nameSuffix,
            onSelected = { onProfileChange(profile.copy(nameSuffix = it)) },
        )
        OutlinedTextField(
            value = profile.age.toString(),
            onValueChange = { raw ->
                val parsed = raw.filter { it.isDigit() }.toIntOrNull()
                if (parsed != null) {
                    onProfileChange(profile.copy(age = parsed))
                } else if (raw.isEmpty()) {
                    onProfileChange(profile.copy(age = CoachCreateProfile.MIN_AGE))
                }
            },
            label = { Text(stringResource(R.string.new_coach_age)) },
            supportingText = {
                Text(
                    stringResource(
                        R.string.new_coach_age_hint,
                        CoachCreateProfile.MIN_AGE,
                        CoachCreateProfile.MAX_AGE,
                    ),
                )
            },
            isError = validationError is CoachCreateValidationError.AgeOutOfRange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        AlmaMaterPicker(
            schools = schools,
            selectedTeamId = profile.almaMaterTeamId,
            onSelected = { onProfileChange(profile.copy(almaMaterTeamId = it)) },
        )
        Text(
            text = stringResource(R.string.new_coach_appearance_section),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(top = 8.dp),
        )
        PresetChipRow(
            label = stringResource(R.string.new_coach_skin),
            options = CoachAvatarPresets.skinTones,
            selectedId = profile.skinToneId,
            onSelect = { onProfileChange(profile.copy(skinToneId = it)) },
        )
        PresetChipRow(
            label = stringResource(R.string.new_coach_hair_color),
            options = CoachAvatarPresets.hairColors,
            selectedId = profile.hairColorId,
            onSelect = { onProfileChange(profile.copy(hairColorId = it)) },
        )
        PresetChipRow(
            label = stringResource(R.string.new_coach_hair_style),
            options = CoachAvatarPresets.hairStyles,
            selectedId = profile.hairStyleId,
            onSelect = { onProfileChange(profile.copy(hairStyleId = it)) },
        )
        PresetChipRow(
            label = stringResource(R.string.new_coach_beard),
            options = CoachAvatarPresets.beardStyles,
            selectedId = profile.beardStyleId,
            onSelect = { onProfileChange(profile.copy(beardStyleId = it)) },
        )
        val preview = profile.displayName()
        if (preview.isNotEmpty()) {
            Text(
                text = stringResource(R.string.new_coach_display_preview, preview),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        validationError?.let { err ->
            Text(
                text = validationErrorMessage(err),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun validationErrorMessage(error: CoachCreateValidationError): String = when (error) {
    CoachCreateValidationError.FirstNameRequired -> stringResource(R.string.new_coach_err_first)
    CoachCreateValidationError.LastNameRequired -> stringResource(R.string.new_coach_err_last)
    is CoachCreateValidationError.AgeOutOfRange -> stringResource(
        R.string.new_coach_err_age,
        CoachCreateProfile.MIN_AGE,
        CoachCreateProfile.MAX_AGE,
    )
    CoachCreateValidationError.InvalidAvatar -> stringResource(R.string.new_coach_err_avatar)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuffixDropdown(
    selected: CoachNameSuffix,
    onSelected: (CoachNameSuffix) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = suffixLabel(selected),
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.new_coach_suffix)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            CoachNameSuffix.entries.forEach { suffix ->
                DropdownMenuItem(
                    text = { Text(suffixLabel(suffix)) },
                    onClick = {
                        onSelected(suffix)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun suffixLabel(suffix: CoachNameSuffix): String = when (suffix) {
    CoachNameSuffix.None -> stringResource(R.string.new_coach_suffix_none)
    CoachNameSuffix.Jr -> stringResource(R.string.new_coach_suffix_jr)
    CoachNameSuffix.Second -> "II"
    CoachNameSuffix.Third -> "III"
    CoachNameSuffix.Fourth -> "IV"
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun AlmaMaterPicker(
    schools: List<TeamDisplayOverlay>,
    selectedTeamId: String?,
    onSelected: (String?) -> Unit,
) {
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = schools.firstOrNull { it.teamId == selectedTeamId }?.displayName
        ?: stringResource(R.string.new_coach_alma_none)
    val filtered = remember(schools, query) {
        val q = query.trim().lowercase()
        if (q.isEmpty()) schools.take(12)
        else schools.filter {
            it.displayName.lowercase().contains(q) ||
                it.shortName.lowercase().contains(q)
        }.take(12)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = if (expanded) query else selectedLabel,
            onValueChange = {
                query = it
                expanded = true
            },
            label = { Text(stringResource(R.string.new_coach_alma)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.new_coach_alma_none)) },
                onClick = {
                    onSelected(null)
                    query = ""
                    expanded = false
                },
            )
            filtered.forEach { school ->
                DropdownMenuItem(
                    text = { Text(school.displayName) },
                    onClick = {
                        onSelected(school.teamId)
                        query = ""
                        expanded = false
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PresetChipRow(
    label: String,
    options: List<dev.novux.dynasty.cbb.core.coach.AvatarPresetOption>,
    selectedId: String,
    onSelect: (String) -> Unit,
) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelLarge)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 4.dp),
        ) {
            options.forEach { option ->
                FilterChip(
                    selected = option.id == selectedId,
                    onClick = { onSelect(option.id) },
                    label = { Text(option.label) },
                )
            }
        }
    }
}
