package io.github.limuyang2.xxhash.webapp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import io.github.limuyang2.xxhash.demo.XxHashDemoApp

private const val DEFAULT_INPUT = "Hello, World!"

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        XxHashDemoApp()
    }
}
