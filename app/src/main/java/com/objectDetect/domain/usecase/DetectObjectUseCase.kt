package com.objectDetect.domain.usecase

import android.graphics.Bitmap
import com.objectDetect.core.model.DetectedObject
import com.objectDetect.data.repository.ObjectDetectionRepository

class DetectObjectsUseCase(
    private val repository: ObjectDetectionRepository
) {
    suspend operator fun invoke(bitmap: Bitmap): Pair<List<DetectedObject>, String?> {
        return repository.detectObjects(bitmap)
    }
}