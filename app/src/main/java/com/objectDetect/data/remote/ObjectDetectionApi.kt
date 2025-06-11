package com.objectDetect.data.remote

import android.graphics.Bitmap
import com.objectDetect.util.bitmapToBase64
import com.objectDetect.util.resizeBitmapToMax
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.Json

/**
 * Interface for object detection API to process images and return detection results.
 *
 * @author udit
 */
interface ObjectDetectionApi {
    /**
     * Sends the bitmap to the remote API and returns the workflow outputs response.
     */
    suspend fun detectObjectsRaw(bitmap: Bitmap): WorkflowOutputsResponse
}
/**
 * Ktor-based implementation of [ObjectDetectionApi] for calling the Roboflow workflow endpoint.
 * Handles bitmap resizing, base64 encoding, and JSON parsing.
 *
 * @author udit
 */
class KtorObjectDetectionApi(
    private val httpClient: HttpClient
) : ObjectDetectionApi {

    override suspend fun detectObjectsRaw(bitmap: Bitmap): WorkflowOutputsResponse{
        val resizedBitmap = resizeBitmapToMax(bitmap)
        val base64Image = bitmapToBase64(resizedBitmap)

        val requestBody = RoboflowRequest(
            apiKey = "UCHcHON5aFbJwsqoixFN",
            inputs = RoboflowInputs(
                image = RoboflowImageInput(type = "base64", value = base64Image)
            )
        )

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
}
