package com.objectDetect.di

import com.objectDetect.data.remote.KtorObjectDetectionApi
import com.objectDetect.data.remote.ObjectDetectionApi
import com.objectDetect.data.repository.ObjectDetectionRepository
import com.objectDetect.domain.usecase.DetectObjectsUseCase
import com.objectDetect.presentation.viewModel.ImagePickerViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module providing dependencies for object detection, networking, and ViewModel.
 *
 * @author udit
 */
val appModule = module {
    single<ObjectDetectionApi> { KtorObjectDetectionApi(get()) }
    single { ObjectDetectionRepository(get()) }
    single { DetectObjectsUseCase(get()) }
    single { HttpClientProvider.create() }
    viewModelOf(::ImagePickerViewModel)
}