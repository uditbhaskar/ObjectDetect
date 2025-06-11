package com.objectDetect.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.core.graphics.scale
import java.io.ByteArrayOutputStream

/**
 * Decodes a base64-encoded string into a Bitmap.
 *
 * @author udit
 */
fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
    return try {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: Exception) {
        print(e.message)
        null
    }
}

/**
 * Resizes the given bitmap to fit within the specified max width and height, preserving aspect ratio.
 *
 * @author udit
 */
fun resizeBitmapToMax(bitmap: Bitmap, maxWidth: Int = 2048, maxHeight: Int = 2048): Bitmap {
    val width = bitmap.width
    val height = bitmap.height

    if (width <= maxWidth && height <= maxHeight) return bitmap

    val aspectRatio = width.toFloat() / height.toFloat()
    val (newWidth, newHeight) = if (aspectRatio > 1) {
        maxWidth to (maxWidth / aspectRatio).toInt()
    } else {
        (maxHeight * aspectRatio).toInt() to maxHeight
    }

    return bitmap.scale(newWidth, newHeight)
}

/**
 * Encodes the given bitmap as a base64 JPEG string.
 *
 * @author udit
 */
fun bitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}