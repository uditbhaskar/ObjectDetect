package com.objectDetect.core.model

import kotlinx.serialization.Serializable

@Serializable
data class DetectedObject(
    val label: String,
    val confidence: Float,
    val boundingBox: BoundingBox
)

@Serializable
data class BoundingBox(
    val left: Float,
    val top: Float,
    val right: Float,
    val bottom: Float
)