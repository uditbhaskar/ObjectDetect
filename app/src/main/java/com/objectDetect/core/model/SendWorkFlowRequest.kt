package com.objectDetect.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Input image specification for API requests.
 *
 * @author udit
 */
@Serializable
data class RoboflowImageInput(
    val type: String = "base64",
    val value: String
)

/**
 * Wrapper for API image input.
 *
 * @author udit
 */
@Serializable
data class RoboflowInputs(
    val image: RoboflowImageInput
)

/**
 * Request body for API inference.
 *
 * @author udit
 */
@Serializable
data class RoboflowRequest(
    @SerialName("api_key") val apiKey: String,
    val inputs: RoboflowInputs
)