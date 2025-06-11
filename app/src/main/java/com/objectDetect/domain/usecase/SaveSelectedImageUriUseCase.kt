package com.objectDetect.domain.usecase

import android.net.Uri

/**
 * Use case for saving the selected image URI.
 * @author udit
 */
class SaveSelectedImageUriUseCase {
    operator fun invoke(uri: Uri): Uri {
        return uri
    }
}