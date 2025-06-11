package com.objectDetect.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WorkflowOutputsResponse(
    val outputs: List<WorkflowResponse>
)

@Serializable
data class WorkflowResponse(
    @SerialName("count_objects") val countObjects: Int,
    @SerialName("output_image") val outputImage: OutputImage,
    @SerialName("model_predictions") val modelPredictions: ModelPredictions
)

@Serializable
data class ModelPredictions(
    val image: PredictionImage? = null,
    val predictions: List<Prediction>
)
@Serializable
data class PredictionImage(
    val width: Int? = null,
    val height: Int? = null
)
@Serializable
data class OutputImage(
    val type: String,
    val value: String // base64-encoded image
)

@Serializable
data class OutputPredictions(
    val type: String,
    val value: List<Prediction>
)

@Serializable
data class Prediction(
    val width: Float,
    val height: Float,
    val x: Float,
    val y: Float,
    val confidence: Float,
    @SerialName("class") val label: String
)




@Serializable
data class RoboflowImageInput(
    val type: String = "base64",
    val value: String
)

@Serializable
data class RoboflowInputs(
    val image: RoboflowImageInput
)

@Serializable
data class RoboflowRequest(
    @SerialName("api_key") val apiKey: String,
    val inputs: RoboflowInputs
)
