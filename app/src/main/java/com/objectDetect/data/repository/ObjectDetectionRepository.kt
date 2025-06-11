package com.objectDetect.data.repository

import android.graphics.Bitmap
import com.objectDetect.core.model.BoundingBox
import com.objectDetect.core.model.DetectedObject
import com.objectDetect.data.remote.ObjectDetectionApi
import com.objectDetect.data.remote.WorkflowOutputsResponse

/**
 * Repository for performing object detection using the remote API.
 *
 * @author udit
 */
class ObjectDetectionRepository(
    private val api: ObjectDetectionApi
) {
    /**
     * Detects objects in the given bitmap and returns the results and processed image base64.
     *
     * @author udit
     */
    suspend fun detectObjects(bitmap: Bitmap): Pair<List<DetectedObject>, String?> {
        val response: WorkflowOutputsResponse = api.detectObjectsRaw(bitmap)
        val workflow = response.outputs.firstOrNull()
        val predictions = workflow?.modelPredictions?.predictions ?: emptyList()
        val outputImageBase64 = workflow?.outputImage?.value

        val detectedObjects = predictions.map {
            DetectedObject(
                label = it.label,
                confidence = it.confidence,
                boundingBox = BoundingBox(
                    left = it.x - it.width / 2f,
                    top = it.y - it.height / 2f,
                    right = it.x + it.width / 2f,
                    bottom = it.y + it.height / 2f
                )
            )
        }
        return Pair(detectedObjects, outputImageBase64)
    }
}