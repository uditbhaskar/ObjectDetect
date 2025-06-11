package com.objectDetect.core.model

import kotlinx.serialization.Serializable

/**
 * Represents a detected object with label, confidence, and bounding box.
 *
 * @author udit
 */
@Serializable
data class DetectedObject(
    val label: String,
    val confidence: Float,
    val boundingBox: BoundingBox
)

/**
 * Defines the coordinates of a bounding box around a detected object.
 *
 * @author udit
 */
@Serializable
data class BoundingBox(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
)