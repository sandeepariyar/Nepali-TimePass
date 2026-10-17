package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

/** Bounded form layout: input fields do not participate in AlertDialog text intrinsics. */
@Composable
internal fun HomeFilmLibraryDialog(
    ne: Boolean, movieCount: Int, syncing: Boolean, search: String,
    onSearchChange: (String) -> Unit, onRefresh: () -> Unit, onSearch: () -> Unit,
    onCustom: () -> Unit, onDismiss: () -> Unit
) {
    val title = if (ne) "चलचित्र सङ्ग्रह" else "Film library"
    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(28.dp), color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.testTag("film_library_dialog").semantics { paneTitle = title }) {
            Column(Modifier.fillMaxWidth().heightIn(max = 520.dp).padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(title, style = MaterialTheme.typography.headlineSmall)
                Column(Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(if (ne) "$movieCount चलचित्र तयार" else "$movieCount films ready")
                    Text(if (syncing) { if (ne) "चलचित्र थपिँदैछन्…" else "Fetching films…" }
                        else { if (ne) "अनलाइन चलचित्र र पोस्टर" else "Online films and posters" })
                    OutlinedButton(onClick = onRefresh, enabled = !syncing, modifier = Modifier.fillMaxWidth()) {
                        Text(if (ne) "चलचित्र अपडेट" else "Refresh film library")
                    }
                    OutlinedTextField(value = search, onValueChange = onSearchChange, singleLine = true,
                        label = { Text(if (ne) "फिल्म खोज्नुहोस्" else "Search online films") },
                        modifier = Modifier.fillMaxWidth().testTag("film_search_field"))
                    Button(onClick = onSearch, enabled = search.isNotBlank() && !syncing,
                        modifier = Modifier.fillMaxWidth()) {
                        Text(if (ne) "खोजेर थप्नुहोस्" else "Search & add")
                    }
                    TextButton(onClick = onCustom, modifier = Modifier.fillMaxWidth().testTag("add_custom_movie_button")) {
                        Text(if (ne) "+ आफ्नै चलचित्र थप्नुहोस्" else "+ Add your own film")
                    }
                }
                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text(if (ne) "बन्द" else "Close")
                }
            }
        }
    }
}
