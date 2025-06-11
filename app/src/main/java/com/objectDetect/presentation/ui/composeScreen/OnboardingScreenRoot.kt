package com.objectDetect.presentation.ui.composeScreen



import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun OnboardingScreenRoot(
    onNavigateNext: (String) -> Unit
) {
    // Launch image picker
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val encoded = URLEncoder.encode(it.toString(), StandardCharsets.UTF_8.toString())
            onNavigateNext(encoded)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { launcher.launch(IMAGE_SELECTED) }) {
            Text(OPEN_PHOTOS_TEXT)
        }
    }
}

const val IMAGE_SELECTED = "image/*"
const val OPEN_PHOTOS_TEXT = "Open photos"