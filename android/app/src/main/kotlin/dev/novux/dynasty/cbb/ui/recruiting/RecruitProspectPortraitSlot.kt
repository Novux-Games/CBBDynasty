package dev.novux.dynasty.cbb.ui.recruiting

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.core.recruiting.RecruitProspect
import dev.novux.dynasty.cbb.core.recruiting.RecruitProspectPortraitPolicy
import dev.novux.dynasty.cbb.ui.portrait.PresetHeadshot

@Composable
fun RecruitProspectPortraitSlot(
    prospect: RecruitProspect,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
) {
    @Suppress("UNUSED_VARIABLE")
    val qaId = RecruitProspectPortraitPolicy.MEGA_ID
    PresetHeadshot(
        avatarPresetId = RecruitProspectPortraitPolicy.presetForProspect(prospect.prospectId),
        modifier = modifier,
        size = size,
        contentDescription = null,
    )
}