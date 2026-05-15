package io.github.limuyang2.xxhash.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import io.github.limuyang2.xxhash.lib.xxh32
import io.github.limuyang2.xxhash.lib.xxh3As128
import io.github.limuyang2.xxhash.lib.xxh3As64
import io.github.limuyang2.xxhash.lib.xxh64

@Composable
fun XxHashDemoApp() {
    var input by remember { mutableStateOf("Hello, World!") }
    val summary = remember(input) { calculateHashSummary(input) }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("XXHash Demo", style = MaterialTheme.typography.headlineSmall)

                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    label = { Text("Input") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = false,
                    maxLines = 5
                )

                HashCard("XXH32", summary.xxh32)
                HashCard("XXH64", summary.xxh64)
                HashCard("XXH3-64", summary.xxh3As64)
                HashCard("XXH3-128", summary.xxh3As128)
            }
        }
    }
}

@Composable
private fun HashCard(label: String, hex: String) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, style = MaterialTheme.typography.labelMedium)
            Spacer(Modifier.width(12.dp))
            Text(
                hex,
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private fun calculateHashSummary(input: String): HashSummary {
    val data = input.encodeToByteArray()
    val h128 = data.xxh3As128()

    return HashSummary(
        xxh32 = data.xxh32().toFixedHex(),
        xxh64 = data.xxh64().toFixedHex(),
        xxh3As64 = data.xxh3As64().toFixedHex(),
        xxh3As128 = h128[1].toFixedHex() + h128[0].toFixedHex(),
    )
}

private fun Long.toFixedHex(): String =
    toULong().toString(radix = 16).padStart(16, '0')
