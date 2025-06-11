package com.objectDetect.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Root response containing a list of workflow outputs from the object detection API.
 *
 * @author udit
 */
@Serializable
data class WorkflowOutputsResponse(
    val outputs: List<WorkflowResponse>
)

/**
 * Represents a single workflow output, including predictions and processed image.
 *
 * @author udit
 */
@Serializable
data class WorkflowResponse(
    @SerialName("count_objects") val countObjects: Int,
    @SerialName("output_image") val outputImage: OutputImage,
    @SerialName("model_predictions") val modelPredictions: ModelPredictions
)

/**
 * Contains prediction image metadata and list of detected predictions.
 *
 * @author udit
 */
@Serializable
data class ModelPredictions(
    val image: PredictionImage? = null,
    val predictions: List<Prediction>
)

/**
 * Metadata about the prediction image dimensions.
 *
 * @author udit
 */
@Serializable
data class PredictionImage(
    val width: Int? = null,
    val height: Int? = null
)

/**
 * Represents the processed output image (usually base64-encoded).
 *
 * @author udit
 */
@Serializable
data class OutputImage(
    val type: String,
    val value: String
)

/**
 * Represents a single detected object prediction.
 *
 * @author udit
 */
@Serializable
data class Prediction(
    val width: Float,
    val height: Float,
    val x: Float,
    val y: Float,
    val confidence: Float,
    @SerialName("class") val label: String
)

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