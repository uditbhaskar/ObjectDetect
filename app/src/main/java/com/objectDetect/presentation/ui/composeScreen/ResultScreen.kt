package com.objectDetect.presentation.ui.composeScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.objectDetect.presentation.viewModel.ImagePickerViewModel
import com.objectDetect.util.AppConstants
import com.objectDetect.util.decodeBase64ToBitmap
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import java.net.URLDecoder
import kotlin.text.Charsets.UTF_8

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
    val decodedUri = imageUri?.let { URLDecoder.decode(it, UTF_8.toString()) }
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
        if (bitmap != null) {
            viewModel.detectObjects(bitmap)
        }
    }

    // Observe loading and output image base64
    val loading by viewModel.loading.collectAsState()

    // Observe loading and output image base64
    val error by viewModel.error.collectAsState()

    // Observe output image base64 from ViewModel
    val outputImageBase64 by viewModel.outputImageBase64.collectAsState()

    // Decode the output image base64 to Bitmap when it changes
    LaunchedEffect(outputImageBase64) {
        outputBitmap = outputImageBase64?.let { base64 ->
            withContext(dispatcher) { decodeBase64ToBitmap(base64) }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF2193b0), Color(0xFF6dd5ed))
                )
            )
    ) {
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
                    .padding(bottom = 16.dp)
                    .shadow(10.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.97f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        AppConstants.ORIGINAL_IMAGE_LABEL,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2193b0)
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1.7f)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { fullScreenImage = it }
                        )
                    } ?: Text(
                        AppConstants.NO_IMAGE_LOADED,
                        color = Color.Red,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    ).run {
                        println(error.toString())
                    }
                }
            }

            // Detected Image Card (from output image base64)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
                    .shadow(10.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.97f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        AppConstants.DETECTION_IMAGE_LABEL,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2193b0)
                        )
                    )
                    Spacer(Modifier.height(8.dp))
                    when {
                        loading -> CircularProgressIndicator(color = Color(0xFF2193b0))
                        outputBitmap != null -> Image(
                            bitmap = outputBitmap!!.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1.7f)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { fullScreenImage = outputBitmap }
                        )
                        else -> Text(
                            AppConstants.DETECTION_IMAGE_NOT_AVAILABLE,
                            color = Color.Red,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Go to main button at the bottom
            Button(
                onClick = onBackToMain,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2193b0))
            ) {
                Text(
                    AppConstants.GO_TO_MAIN_BUTTON,
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
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
                            contentDescription = AppConstants.CLOSE_IMAGE,
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}