package com.objectDetect.domain.usecase

import android.graphics.Bitmap
import com.objectDetect.core.model.DetectedObject
import com.objectDetect.data.repository.ObjectDetectionRepository

/**
 * Use case for running object detection on a bitmap and returning results.
 *
 * @author udit
 */
class DetectObjectsUseCase(
    private val repository: ObjectDetectionRepository
) {
    /**
     * Runs object detection and returns detected objects and processed image base64.
     *
     * @author udit
     */
    suspend operator fun invoke(bitmap: Bitmap): Pair<List<DetectedObject>, String?> {
        return repository.detectObjects(bitmap)
    }
}