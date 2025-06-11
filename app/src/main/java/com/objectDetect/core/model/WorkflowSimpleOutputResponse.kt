package com.objectDetect.core.model

import kotlinx.serialization.Serializable

@Serializable
/**
 * Response containing a list of simple output wrappers.
 * @author udit
 */
data class SimpleWorkflowOutputsResponse(
    val outputs: List<SimpleOutputWrapper>
)

@Serializable
/**
 * Wrapper for a single base64 output result.
 * @author udit
 */
data class SimpleOutputWrapper(
    val output: SimpleBase64Output,
)

@Serializable
/**
 * Represents a base64-encoded output with its type.
 * @author udit
 */
data class SimpleBase64Output(
    val type: String,
    val value: String
)