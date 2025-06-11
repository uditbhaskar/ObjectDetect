package com.objectDetect.di

import com.objectDetect.data.remote.KtorObjectDetectionApi
import com.objectDetect.data.remote.ObjectDetectionApi
import com.objectDetect.data.repository.ObjectDetectionRepository
import com.objectDetect.domain.usecase.DetectObjectsUseCase
import com.objectDetect.domain.usecase.SaveSelectedImageUriUseCase
import com.objectDetect.presentation.viewModel.ImagePickerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<ObjectDetectionApi> { KtorObjectDetectionApi(get()) }
    single { ObjectDetectionRepository(get()) }
    single { DetectObjectsUseCase(get()) }
    single { SaveSelectedImageUriUseCase() }
    single { HttpClientProvider.create() }
    viewModel { ImagePickerViewModel(get(), get()) }
}