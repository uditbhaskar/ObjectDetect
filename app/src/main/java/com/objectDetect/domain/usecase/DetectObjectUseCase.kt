package com.objectDetect.domain.usecase

import android.graphics.Bitmap
import com.objectDetect.data.repository.IObjectDetectionRepository

/**
 * Interface for the DetectObjects use case.
 */
interface IDetectObjectsUseCase {
    /**
     * Runs object detection and returns the processed image base64 string from the workflow output.
     */
    suspend operator fun invoke(bitmap: Bitmap): String?
}

/**
 * Use case for running object detection on a bitmap and returning results.
 *
 * @author udit
 */
class DetectObjectsUseCase(
    private val repository: IObjectDetectionRepository
) : IDetectObjectsUseCase {
    /**
     * Runs object detection and returns the processed image base64 string from the workflow output.
     * The base64 string is extracted from outputs[0].output.value in the response.
     *
     * @author udit
     */
    override suspend operator fun invoke(bitmap: Bitmap): String? {
        return repository.detectObjects(bitmap)
    }
}