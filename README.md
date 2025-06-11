# ObjectDetect

ObjectDetect is a modern Android app that lets users select an image from their gallery and performs cloud-based object detection using a Roboflow workflow. The app displays both the original and processed images (with bounding boxes), supports full-screen viewing with zoom/pan, and is built with Jetpack Compose, Ktor, and Koin using Clean Architecture.

---

## ✨ Features

- Select a smaller resolution photof from gallery.
- Cloud object detection via Roboflow workflow API.
- View both original and detected images side by side.
- Full-screen image viewing with pinch-to-zoom and pan support.
- Modern Material3 UI with gradients and cards.
- Clean, testable architecture with Koin DI and Ktor networking.

---

## 📹 Demo
https://github.com/user-attachments/assets/772ca950-f742-4f55-b264-63f53444c7fd

---

## 🏗️ Architecture

- **Jetpack Compose** for UI
- **Koin** for Dependency Injection
- **Ktor** for Networking
- **MVVM + Clean Architecture**: Domain/usecase, repository, remote/data, and UI layers

---

## 🗂️ Project Structure

```
app/
  core/model/           # Data models (request/response)
  data/remote/          # API interfaces and implementations
  data/repository/      # Repository interfaces and implementations
  di/                   # Dependency injection modules
  domain/usecase/       # Use case interfaces and implementations
  presentation/         # ViewModels and Compose screens
  util/                 # Utility functions and constants
```

## 🚀 Getting Started

1. **Clone the repo:**
   ```bash
   git clone https://github.com/yourusername/ObjectDetect.git
   cd ObjectDetect
   ```

2. **Open in Android Studio** (Giraffe or newer recommended).

3. **Set up your Roboflow API key and workflow endpoint:**
   - Edit `KtorObjectDetectionApi.kt` and replace the API key and endpoint with your own from [Roboflow](https://roboflow.com/).

4. **Build and run on your device or emulator.**

## 🛠️ Configuration

- **API Key:**  
  Set your Roboflow API key in `KtorObjectDetectionApi`.
- **Image Size Limit:**  
  Images are resized to max 2048x2048 before upload.

## 🧩 Dependencies

- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Koin](https://insert-koin.io/)
- [Ktor](https://ktor.io/)
- [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)
- [Timber](https://github.com/JakeWharton/timber) for logging
