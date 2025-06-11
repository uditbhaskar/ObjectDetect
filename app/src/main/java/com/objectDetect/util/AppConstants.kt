package com.objectDetect.util

/**
 * App-wide constants.
 * Navigation route constants are grouped below.
 * @author udit
 */
object AppConstants {
    //Navigation Routes
    const val NAV_ONBOARD = "onboard"
    const val NAV_RESULT = "result"

    //Onboarding Screen
    const val IMAGE_SELECTED = "image/*"
    const val OPEN_PHOTOS_TEXT = "Open photos"
    const val ONBOARDING_HEADLINE = "Welcome to ObjectDetect!"
    const val ONBOARDING_SUBTITLE = "Select a photo from your gallery to get started with object detection."

    // Result Screen
    const val ORIGINAL_IMAGE_LABEL = "Original image"
    const val DETECTION_IMAGE_LABEL = "Object detection image"
    const val GO_TO_MAIN_BUTTON = "Go to main"
    const val NO_IMAGE_LOADED = "No image loaded."
    const val DETECTION_IMAGE_NOT_AVAILABLE = "Detection image not available."
    const val CLOSE_IMAGE = "Close"
}