package com.objectDetect.data.repository

import android.graphics.Bitmap
import com.objectDetect.data.remote.ObjectDetectionApi
import com.objectDetect.core.model.SimpleWorkflowOutputsResponse

/**
 * Interface for object detection repository.
 */
interface IObjectDetectionRepository {
    /**
     * Detects objects in the given bitmap and returns the base64 image string from the workflow output.
     *
     * @author udit
     */
    suspend fun detectObjects(bitmap: Bitmap): String?
}

/**
 * Repository for performing object detection using the remote API.
 *
 * @author udit
 */
class ObjectDetectionRepository(
    private val api: ObjectDetectionApi
) : IObjectDetectionRepository {
    /**
     * Detects objects in the given bitmap and returns the base64 image string from the workflow output.
     * The base64 string is extracted from outputs[0].output.value in the response.
     *
     * @author udit
     */
    override suspend fun detectObjects(bitmap: Bitmap): String? {
        val response: SimpleWorkflowOutputsResponse = api.detectObjectsRaw(bitmap)
        return response.outputs.firstOrNull()?.output?.value
    }
}