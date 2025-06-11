package com.objectDetect.presentation.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.objectDetect.core.model.DetectedObject
import com.objectDetect.domain.usecase.DetectObjectsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for running object detection and exposing detection results and processed image state.
 *
 * @author udit
 */
class ImagePickerViewModel(
    private val detectObjectsUseCase: DetectObjectsUseCase
) : ViewModel() {

    private val _detectedObjects = MutableStateFlow<List<DetectedObject>>(emptyList())
    val detectedObjects: StateFlow<List<DetectedObject>> = _detectedObjects.asStateFlow()

    private val _outputImageBase64 = MutableStateFlow<String?>(null)
    val outputImageBase64: StateFlow<String?> = _outputImageBase64.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    /**
     * Runs object detection on the given bitmap and updates state with results and processed image.
     *
     * @author udit
     */
    fun detectObjects(bitmap: Bitmap) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                val (objects, outputImageBase64) = detectObjectsUseCase(bitmap)
                _detectedObjects.value = objects
                _outputImageBase64.value = outputImageBase64
            } catch (e: Exception) {
                _error.value = e.message ?: "Detection failed"
            } finally {
                _loading.value = false
            }
        }
    }
}