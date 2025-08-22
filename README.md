# GitHub User Search App

A modern Android mobile application built with Kotlin and Jetpack Compose that allows users to search for GitHub users and view detailed information about them.

## Features

### ✅ Implemented Features
- **User Search**: Search for GitHub users using the GitHub Search API
- **MVVM Architecture**: Clean architecture with ViewModel and Repository pattern
- **Modern UI**: Built with Jetpack Compose and Material Design 3
- **API Integration**: Uses Retrofit to communicate with GitHub REST API
- **Image Loading**: Efficient image loading with Coil for profile pictures
- **Pagination**: Load more search results with a "Load More" button
- **Navigation**: Navigate between search results and user details
- **User Details**: Comprehensive user information display
- **Error Handling**: Proper error handling and loading states

### 🔄 Search Functionality
- Search for GitHub users by username
- Real-time search with GitHub API
- Pagination support (30 users per page)
- Display user ID, profile picture, type, and search score

### 👤 User Details Screen
- Complete user profile information
- Profile statistics (repositories, followers, following)
- Contact information and location
- Account creation and update dates
- Professional information (company, blog, bio)

## Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Networking**: Retrofit + OkHttp
- **Image Loading**: Coil
- **Navigation**: Navigation Compose
- **Coroutines**: Kotlin Coroutines for async operations
- **Material Design**: Material Design 3 components

## Project Structure

```
app/src/main/java/com/example/github_user_search_app/
├── data/
│   ├── model/
│   │   ├── User.kt              # Basic user model for search results
│   │   ├── UserDetail.kt        # Detailed user model for profile
│   │   └── SearchResult.kt      # Search API response model
│   ├── network/
│   │   ├── Api.kt               # Retrofit API interface
│   │   └── RetrofitInstance.kt  # Retrofit configuration
│   └── repository/
│       └── UserRepository.kt    # Data repository layer
├── navigation/
│   └── AppNavigation.kt         # Navigation setup
├── ui/
│   ├── components/
│   │   ├── SearchBar.kt         # Search input component
│   │   └── UsersList.kt         # User list item component
│   ├── screens/
│   │   ├── SearchScreen.kt      # Main search screen
│   │   └── UserDetailScreen.kt  # User details screen
│   └── theme/                   # App theming
├── viewmodel/
│   ├── SearchViewModel.kt       # Search screen ViewModel
│   └── UserDetailViewModel.kt   # User details ViewModel
└── MainActivity.kt              # Main activity entry point
```

## API Endpoints Used

### GitHub Search API
- **Search Users**: `GET /search/users?q={query}&page={page}&per_page=30`
- **User Details**: `GET /users/{username}`

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 21+ (API level 21)
- Kotlin 1.8+

### Installation
1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the app on an emulator or physical device

### Build Configuration
- **Compile SDK**: 35
- **Target SDK**: 34
- **Min SDK**: 21
- **Kotlin Compiler Extension**: 1.7.0

## Key Components

### SearchViewModel
- Manages search state and pagination
- Handles API calls through repository
- Maintains search query and results

### UserDetailViewModel
- Loads detailed user information
- Manages user detail screen state
- Handles API errors gracefully

### UserRepository
- Central data access point
- Abstracts API calls from ViewModels
- Provides clean data layer interface

## UI Components

### SearchBar
- Clean search input with icons
- Clear button functionality
- Search button for manual search

### UsersList
- User profile picture display
- User information layout
- Clickable items for navigation

### UserDetailScreen
- Comprehensive user profile view
- Statistics cards
- Organized information sections

## Future Enhancements

- [ ] **Infinite Scroll**: Replace manual "Load More" with automatic pagination
- [ ] **Offline Support**: Cache user data for offline viewing
- [ ] **Search Filters**: Add filters for user type, location, etc.
- [ ] **Favorites**: Save favorite users locally
- [ ] **Dark Theme**: Implement dark/light theme toggle
- [ ] **Browser Integration**: Open GitHub profiles in external browser
- [ ] **Share Functionality**: Share user profiles
- [ ] **Unit Tests**: Add comprehensive testing

## Contributing

This is a final project for an Android mobile development internship. The project demonstrates:

- Modern Android development practices
- Clean architecture principles
- API integration skills
- UI/UX design with Material Design
- Navigation implementation
- State management with ViewModels

## License

This project is created for educational purposes as part of an internship program.

## Acknowledgments

- GitHub for providing the public API
- Android team for Jetpack Compose
- Material Design team for design guidelines
- Retrofit and Coil teams for excellent libraries 