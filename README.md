Simple and clean App showing Movies from free API 
1. Jetpack Compose
2. Compose Navigation
3. Compose Bottom navigation
4. Compose native animations
5. ROOM for data storage
6. Data store instead of shared preferences
7. MVVM for architecture
8. Material3
9. Retrofit for Api calls
10. Koin for DI

Project structure >>
app/
 ├── src/main/java/com/example/kostovapp/
 │   ├── **api/**               → API service & Retrofit provider
 │   │   ├── TMDbApiService.kt
 │   │   ├── provideTMDbApiService.kt
 │   │
 │   ├── **data/**              → Handles data sources (Repository, Room, DataStore)
 │   │   ├── datastore/         → DataStore for persistent storage
 │   │   │   ├── DataStoreManager.kt
 │   │   ├── model/             → Data models (Movie, Response objects)
 │   │   │   ├── Movie.kt
 │   │   │   ├── TMDbResponse.kt
 │   │   │   ├── VideoResponse.kt
 │   │   ├── repository/        → Repository pattern implementation
 │   │   │   ├── MovieRepository.kt
 │   │   ├── room/              → Room database components
 │   │   │   ├── Database.kt
 │   │   │   ├── MovieDao.kt
 │   │   │   ├── MovieEntity.kt
 │   │
 │   ├── **di/**                → Dependency Injection (Koin)
 │   │   ├── AppModule.kt
 │   │
 │   ├── **ui/**                → UI-related components
 │   │   ├── animations/        → Custom animations
 │   │   │   ├── BouncingImage.kt
 │   │   ├── components/        → Reusable UI components
 │   │   │   ├── MovieItem.kt
 │   │   │   ├── PullToRefreshIndicator.kt
 │   │   ├── screens/           → App screens
 │   │   │   ├── MoviesScreen.kt
 │   │   │   ├── MovieDetailsScreen.kt
 │   │   │   ├── FavoritesScreen.kt
 │   │   │   ├── ForLaterWatchingScreen.kt
 │   │   │   ├── WelcomeScreen.kt
 │   │   ├── theme/             → Theming & Colors
 │   │   │   ├── Color.kt
 │   │   │   ├── Theme.kt
 │   │   │   ├── Type.kt
 │   │
 │   ├── **navigation/**        → Navigation components
 │   │   ├── NavHost.kt
 │   │
 │   ├── **bottomnav/**         → Bottom navigation components
 │   │   ├── BottomNavigationBar.kt
 │   │   ├── BottomNavItem.kt
 │   │
 │   ├── **viewmodel/**         → ViewModels
 │   │   ├── MoviesViewModel.kt
 │   │
 │   ├── **mainactivity/**      → Main application components
 │   │   ├── MainActivity.kt
 │   │   ├── MoviesApp.kt

11. ![Alt text](https://reactnativeexample.com/content/images/2019/01/The-Movie-Guide.jpg)

