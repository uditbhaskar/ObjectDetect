package com.objectDetect.data.remote

import android.graphics.Bitmap
import android.util.Base64
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import androidx.core.graphics.scale

interface ObjectDetectionApi {
    suspend fun detectObjectsRaw(bitmap: Bitmap): WorkflowOutputsResponse
}


class KtorObjectDetectionApi(
    private val httpClient: HttpClient
) : ObjectDetectionApi {

    override suspend fun detectObjectsRaw(bitmap: Bitmap): WorkflowOutputsResponse{
        // Convert bitmap to base64
        val resizedBitmap = resizeBitmapToMax(bitmap)
        val base64Image = bitmapToBase64(resizedBitmap)

        // Build request body
        val requestBody = RoboflowRequest(
            apiKey = "UCHcHON5aFbJwsqoixFN", // <-- replace with your real key
            inputs = RoboflowInputs(
                image = RoboflowImageInput(type = "base64", value = base64Image)
            )
        )

        // Endpoint from your workflow
        val apiUrl = "https://serverless.roboflow.com/infer/workflows/objectdetect-1phvw/detect-count-and-visualize"

        // Make the POST request
        val response: HttpResponse = httpClient.post(apiUrl) {
            contentType(ContentType.Application.Json)
            setBody(requestBody)
        }

        val jsonString = response.bodyAsText()
        return Json { ignoreUnknownKeys = true }
            .decodeFromString<WorkflowOutputsResponse>(jsonString)
    }

    private fun resizeBitmapToMax(bitmap: Bitmap, maxWidth: Int = 2048, maxHeight: Int = 2048): Bitmap {
        val width = bitmap.width
        val height = bitmap.height

        if (width <= maxWidth && height <= maxHeight) return bitmap

        val aspectRatio = width.toFloat() / height.toFloat()
        val (newWidth, newHeight) = if (aspectRatio > 1) {
            // Landscape
            maxWidth to (maxWidth / aspectRatio).toInt()
        } else {
            // Portrait or square
            (maxHeight * aspectRatio).toInt() to maxHeight
        }

        return bitmap.scale(newWidth, newHeight)
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}
