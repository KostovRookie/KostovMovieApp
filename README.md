# 🎬 Kostov Movie App

An elegant Android movie browsing app built with Kotlin, Jetpack Compose, and MVVM architecture. 
Browse, search, and favorite top-rated movies using a public movie ratings API.

---

## 🎥 Features

✅ Search and discover top-rated movies  
✅ View detailed movie info (title, rating, description, poster)  
✅ Add movies to favorites using Jetpack DataStore  
✅ Save movies "For Later Watching" using Room Database  
✅ Compose Navigation between screens  
✅ Beautiful Material 3 UI  
✅ Light/Dark theme support  

---

## 🛠️ Technologies & Architecture

| Category         | Stack                                      |
|------------------|--------------------------------------------|
| **Language**     | Kotlin                                     |
| **UI**           | Jetpack Compose, Material 3                |
| **Architecture** | MVVM (ViewModel, Repository pattern)       |
| **State**        | StateFlow, remember, collectAsState        |
| **Data Layer**   | Retrofit (API), Room (local), DataStore    |
| **Navigation**   | Compose Navigation                         |
| **Dependency Injection** | Koin                              |
| **Gradle**       | Kotlin DSL (build.gradle.kts)              |

---

## 🧱 Project Structure

- `ui/` – All Composables (MovieList, MovieDetail, Favorites, For Later)
- `viewmodel/` – ViewModels using StateFlow and Repository
- `data/` – Models, Retrofit API service, Room DAO
- `di/` – Koin module setup
- `navigation/` – Compose navigation graph

---

## 🔗 API

Movie data is fetched from [movies-ratings2.p.rapidapi.com](https://rapidapi.com/Big0ak/api/movies-ratings2)  
> Note: Requires free API key from RapidAPI

---

## 📦 Setup Instructions

1. Clone the repo  
2. Add your RapidAPI key to the headers inside the `MovieApiService`  
3. Open with Android Studio Electric Eel or later  
4. Run on emulator or physical device (API 24+)

---

## 🤝 Contributions

Pull requests are welcome. Feel free to open an issue or suggest an improvement.

---

## 📄 License

MIT License © 2025 [KostovRookie](https://github.com/KostovRookie)


. ![Alt text](https://reactnativeexample.com/content/images/2019/01/The-Movie-Guide.jpg)

