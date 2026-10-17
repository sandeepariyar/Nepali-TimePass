package com.example.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.model.*

@Composable
internal fun HomeGangEditDialog(team: Team, ne: Boolean, onName: (String) -> Unit,
    onColor: (Long) -> Unit, onShuffle: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(24.dp)) {
            Column(Modifier.fillMaxWidth().heightIn(max = 440.dp).padding(20.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(if (ne) "ग्याङ ${team.id}" else "Gang ${team.id}", style = MaterialTheme.typography.headlineSmall)
                OutlinedTextField(team.name, { onName(it.take(48)) }, maxLines = 2,
                    label = { Text(if (ne) "ग्याङको नाम" else "Gang name") },
                    modifier = Modifier.fillMaxWidth().testTag("team_${team.id}_name"))
                Text(if (ne) "टोलीको रङ" else "Team color")
                Row(Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())) {
                    GangColorPalette.COLORS.forEach { choice ->
                        Box(Modifier.size(48.dp).clip(CircleShape)
                            .selectable(selected = team.colorHex == choice.colorHex, role = Role.RadioButton, onClick = { onColor(choice.colorHex) })
                            .semantics { contentDescription = if (ne) choice.nameNepali else choice.name }, contentAlignment = Alignment.Center) {
                            Box(Modifier.size(28.dp).background(Color(choice.colorHex), CircleShape)
                                .border(if (team.colorHex == choice.colorHex) 3.dp else 0.dp, Color.White, CircleShape))
                        }
                    }
                }
                TextButton(onClick = onShuffle, modifier = Modifier.fillMaxWidth()) { Text(if (ne) "नयाँ नाम" else "Shuffle name") }
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth().testTag("gang_editor_done")) { Text(if (ne) "ठीक छ" else "Done") }
            }
        }
    }
}
