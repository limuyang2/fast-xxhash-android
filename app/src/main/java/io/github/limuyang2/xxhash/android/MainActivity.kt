package io.github.limuyang2.xxhash.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import io.github.limuyang2.xxhash.android.ui.theme.XxhashandroidTheme
import io.github.limuyang2.xxhash.lib.XXHash

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            XxhashandroidTheme {
                XxHashDemoApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun XxHashDemoApp() {
    var input by remember { mutableStateOf("Hello, World!") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("XXHash Demo") })
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Input") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 5
            )

            if (input.isNotEmpty()) {
                val data = input.toByteArray()

                HashCard("XXH32", XXHash.xxh32(data, 0))
                HashCard("XXH64", XXHash.xxh64(data, 0))
                HashCard("XXH3-64", XXHash.xxh3_64bits(data))

                val h128 = XXHash.xxh3_128bits(data)
                HashCard("XXH3-128", low = h128[0], high = h128[1])
            }
        }
    }
}

@Composable
fun HashCard(label: String, hash: Long) {
    val hex = hash.toHexString()
    val clipboard = LocalClipboardManager.current

    Card(
        onClick = { clipboard.setText(AnnotatedString(hex)) },
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

@Composable
fun HashCard(label: String, low: Long, high: Long) {
    val hex = high.toHexString() + low.toHexString()
    val clipboard = LocalClipboardManager.current

    Card(
        onClick = { clipboard.setText(AnnotatedString(hex)) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(label, style = MaterialTheme.typography.labelMedium)
            Text(
                hex,
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
