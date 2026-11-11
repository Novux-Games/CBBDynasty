package dev.novux.dynasty.cbb.ui.advance

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.save.DynastyLoadResult
import dev.novux.dynasty.cbb.core.save.DynastyWeekAdvanceBlockReason
import dev.novux.dynasty.cbb.core.save.LoadedDynasty

fun weekAdvanceBlockedMessage(context: Context, reason: DynastyWeekAdvanceBlockReason): String =
    when (reason) {
        DynastyWeekAdvanceBlockReason.DisciplinePending ->
            context.getString(R.string.advance_blocked_discipline)
    }

suspend fun applyWeekAdvanceResult(
    result: DynastyLoadResult,
    snackbarHostState: SnackbarHostState,
    context: Context,
    onSuccess: (LoadedDynasty) -> Unit,
): Boolean =
    when (result) {
        is DynastyLoadResult.Success -> {
            onSuccess(result.loaded)
            true
        }
        is DynastyLoadResult.WeekAdvanceBlocked -> {
            snackbarHostState.showSnackbar(weekAdvanceBlockedMessage(context, result.reason))
            false
        }
        else -> false
    }
