package dev.novux.dynasty.cbb.ui.dashboard

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.notification.NotificationFeedItem

@Composable
fun CareerBlockNotificationDialog(
    item: NotificationFeedItem,
    onAcknowledge: () -> Unit,
    onOpenDestination: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(item.title) },
        text = { Text(item.body) },
        confirmButton = {
            TextButton(
                onClick = {
                    onAcknowledge()
                    onOpenDestination()
                },
            ) {
                Text(stringResource(R.string.dashboard_notification_modal_continue))
            }
        },
    )
}
