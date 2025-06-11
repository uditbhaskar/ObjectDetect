package com.objectDetect.presentation.ui.composeScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.core.net.toUri
import com.objectDetect.presentation.viewModel.ImagePickerViewModel
import org.koin.androidx.compose.koinViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val ORIGINAL_IMAGE_LABEL = "Original image"
private const val DETECTION_IMAGE_LABEL = "Object detection image"
private const val GO_TO_MAIN_BUTTON = "Go to main"

@Composable
fun ResultScreen(
    imageUri: String?,
    onBackToMain: () -> Unit,
    viewModel: ImagePickerViewModel = koinViewModel()
) {
    val context = LocalContext.current

    // Decode URI and convert string back to Uri
    val decodedUri = imageUri?.let { java.net.URLDecoder.decode(it, java.nio.charset.StandardCharsets.UTF_8.toString()) }
    val uri = remember(decodedUri) { decodedUri?.toUri() }

    // Load bitmap from URI (original image)
    val bitmap = remember(uri) {
        uri?.let {
            try {
                context.contentResolver.openInputStream(it)?.use { input ->
                    BitmapFactory.decodeStream(input)
                }
            } catch (e: Exception) {
                print(e.message.toString())
                null
            }
        }
    }

    // State for output image (with bounding boxes)
    var outputBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Trigger detection when bitmap is loaded
    LaunchedEffect(bitmap) {
        if (bitmap != null && viewModel.detectedObjects.value.isEmpty()) {
            viewModel.detectObjects(bitmap)
        }
    }

    // Observe detected objects and output image base64
    val loading by viewModel.loading.collectAsState()

    // Observe output image base64 from ViewModel
    val outputImageBase64 by viewModel.outputImageBase64.collectAsState()

    // Decode the output image base64 to Bitmap when it changes
    LaunchedEffect(outputImageBase64) {
        outputBitmap = outputImageBase64?.let { base64 ->
            withContext(Dispatchers.IO) { decodeBase64ToBitmap(base64) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Original Image Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(ORIGINAL_IMAGE_LABEL, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.7f)
                            .clip(RoundedCornerShape(12.dp))
                    )
                } ?: Text("No image loaded.", color = Color.Red)
            }
        }

        // Detected Image Card (from output image base64)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(DETECTION_IMAGE_LABEL, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                when {
                    loading -> CircularProgressIndicator()
                    outputBitmap != null -> Image(
                        bitmap = outputBitmap!!.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.7f)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    else -> Text("Detection image not available.", color = Color.Red)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Go to main button at the bottom
        Button(
            onClick = onBackToMain,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(GO_TO_MAIN_BUTTON)
        }
    }
}

fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        null
    }
}
