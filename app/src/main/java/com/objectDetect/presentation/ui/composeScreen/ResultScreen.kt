package com.objectDetect.presentation.ui.composeScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.objectDetect.presentation.viewModel.ImagePickerViewModel
import com.objectDetect.util.decodeBase64ToBitmap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import kotlin.text.Charsets.UTF_8

private const val ORIGINAL_IMAGE_LABEL = "Original image"
private const val DETECTION_IMAGE_LABEL = "Object detection image"
private const val GO_TO_MAIN_BUTTON = "Go to main"


/**
 * Displays the original and detected images, showing bounding box results from object detection.
 * Handles image decoding, loading state, and navigation back to main screen.
 * ResultScreen displays both the original and detected images for an object detection workflow.
 *
 * This composable performs the following:
 * - Loads and decodes the original image from the provided URI string.
 * - Automatically triggers object detection on the loaded image using the provided [ImagePickerViewModel].
 * - Observes detection results and decodes the output image (with bounding boxes) from a base64 string.
 * - Shows both the original and detected images in separate cards, with loading indicators as needed.
 * - Allows users to tap either image to view it in a full-screen overlay.
 * - The full-screen overlay supports pinch-to-zoom and pan gestures for detailed inspection of the image,
 *   with zoom levels constrained between 1x and 5x.
 * - Provides a close button to exit the full-screen mode.
 * - Includes a button to navigate back to the main screen.
 *
 * @param imageUri The URI string of the original image to display and process.
 * @param onBackToMain Callback invoked when the user taps the "Go to main" button.
 * @param viewModel The [ImagePickerViewModel] instance used for object detection and state management.
 *
 * @author udit
 */
@Composable
fun ResultScreen(
    imageUri: String?,
    onBackToMain: () -> Unit,
    viewModel: ImagePickerViewModel = koinViewModel(),
    dispatcher: CoroutineDispatcher
) {
    val context = LocalContext.current

    // Decode URI and convert string back to Uri
    val decodedUri = imageUri?.let { java.net.URLDecoder.decode(it, UTF_8.toString()) }
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

    // State for full screen image viewing
    var fullScreenImage by remember { mutableStateOf<Bitmap?>(null) }

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
            withContext(dispatcher) { decodeBase64ToBitmap(base64) }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                                .clickable { fullScreenImage = it }
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
                                .clickable { fullScreenImage = outputBitmap }
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

        // Full screen image dialog/overlay
        if (fullScreenImage != null) {
            // Zoom and pan state
            var scale by remember { mutableFloatStateOf(1f) }
            var offsetX by remember { mutableFloatStateOf(0f) }
            var offsetY by remember { mutableFloatStateOf(0f) }

            val state = rememberTransformableState { zoomChange, panChange, _ ->
                scale = (scale * zoomChange).coerceIn(1f, 5f)
                offsetX += panChange.x
                offsetY += panChange.y
            }

            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = Color.Black.copy(alpha = 0.95f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = fullScreenImage!!.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                translationX = offsetX,
                                translationY = offsetY
                            )
                            .fillMaxWidth()
                            .aspectRatio(1.0f)
                            .transformable(state)
                    )
                    IconButton(
                        onClick = { fullScreenImage = null },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}