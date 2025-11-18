# 💬 Chat Room App

A fully functional, real-time group messaging application built for Android. This project demonstrates modern development practices using **Kotlin**, **Jetpack Compose**, and a **Firebase** backend for authentication and real-time data synchronization.

## ✨ Key Features

* **User Authentication:** Secure Sign Up and Login powered by Firebase Authentication (Email/Password).
* **Room Management:** Users can dynamically create new chat rooms via an `AlertDialog` interface.
* **Real-time Synchronization:** Utilizes **Firestore Snapshot Listeners** and Kotlin Flow for instant updates whenever a new message or room is added.
* **Modern UI:**
    * Clean list view of chat rooms using **Jetpack Compose Cards**.
    * Dynamic chat bubbles that align left (received) or right (sent).
    * Automatic time formatting for messages (e.g., "today 10:30").
* **Robust Navigation:** Controlled navigation flow between all app destinations, including clearing the back stack upon successful login.

## 📐 Architecture & Technology

The project adheres strictly to the **Model-View-ViewModel (MVVM)** pattern, emphasizing a clean separation of concerns.

### Tech Stack
| Component | Technology | Role |
| :--- | :--- | :--- |
| **Language** | Kotlin | Primary programming language. |
| **UI Framework** | Jetpack Compose (Material 3) | Declarative UI toolkit. |
| **Architecture** | MVVM | Structured code separation. |
| **Backend** | Firebase Authentication | Handles user session management. |
| **Database** | Cloud Firestore | Real-time, NoSQL storage for Rooms and Messages. |
| **Concurrency** | Coroutines & Flow | Managing asynchronous operations (network calls, database listeners). |
| **Navigation** | Jetpack Navigation Compose | Handling complex navigation flows and arguments (`roomId`). |



## 🚀 Getting Started

To run this project locally, you must first configure your Firebase project.

1.  **Clone the Repository:**
    ```bash
    git clone [https://github.com/thakur-027/Chat-Room-App.git](https://github.com/thakur-027/Chat-Room-App.git)
    ```
2.  **Firebase Setup (Required):**
    * Go to the **Firebase Console** and create a new project.
    * Enable **Authentication** (Email/Password).
    * Enable **Cloud Firestore** and set up simple security rules (e.g., `allow read, write: if true;` for testing).
    * Download your project's **`google-services.json`** file.
    * Place the `google-services.json` file inside the **`app/`** directory.
3.  **Run:**
    Open the project in Android Studio, ensure Gradle syncs successfully, and run on an Android Emulator or physical device (API 26+ required for date formatting).

## 👨‍💻 Author

**Ayush Thakur**
* **Currently Pursuing:** B.E. in Electronics and Communication Engineering (Sir MVIT, Bengaluru).
* **GitHub:** [@thakur-027](https://github.com/thakur-027)

---
*This project was developed as a comprehensive demonstration of real-time Android Application Development.
