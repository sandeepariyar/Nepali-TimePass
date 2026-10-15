package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RulesDialog(onDismiss: () -> Unit, isNepali: Boolean = false) {
    AlertDialog(onDismissRequest = onDismiss, title = { Text(if (isNepali) "कसरी खेल्ने?" else "A little theatre, a lot of fun.") },
        text = { Column(Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(if (isNepali) "१. दुई ग्याङ बनाउनुहोस्। फोन कलाकारलाई दिनुहोस्। फिल्म अरूलाई नदेखाउनुहोस्।" else "1. Make two gangs. Pass the phone to the actor. Keep the film hidden from everyone else.")
            Text(if (isNepali) "२. नबोली, नगाई, ओठ नचलाइ अभिनय गर्नुहोस्। शब्दको सङ्ख्या औँलाले देखाउनुहोस्।" else "2. Act without speaking, singing or mouthing words. Show the number of words with your fingers.")
            Text(if (isNepali) "३. समयमा मिल्यो भने १ अङ्क। पास वा समय सकिए ० अङ्क। स्किप गर्दा फिल्म मात्र फेरिन्छ।" else "3. A correct guess earns 1 point. Pass or time-up earns 0. Skip changes the film without ending the turn.")
            Text(if (isNepali) "४. समय रोक्न ‘रोक्नुहोस्’ थिच्नुहोस्। अन्तिम १० सेकेन्डमा बत्ती र आवाज फेरिन्छन्।" else "4. Pause freezes the countdown and scene effects. The last ten seconds change the lights and percussion.")
            Text(if (isNepali) "५. पालैपालो खेल्नुहोस्। चाहेको बेला खेलको नतिजा हेर्न सक्नुहुन्छ।" else "5. Take turns. Finish the match whenever your party is ready to crown a winner.")
        } }, confirmButton = { TextButton(onClick = onDismiss) { Text(if (isNepali) "खेलौँ!" else "Let’s play!") } })
}
