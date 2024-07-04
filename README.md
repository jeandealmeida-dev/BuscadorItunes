# 30 Vibes

This is an Android Project that implements an Music library. It can look up for a song by the name than if you press an item list it shows the music detail. Also you can reproduce the music`s preview. 

https://github.com/jeandealmeida-dev/BuscadorItunes/assets/3248076/a0925b4f-4b9a-4c01-a8d4-d93840eec282

## Inspiration

<img width="756" alt="image" src="https://github.com/jeandealmeida-dev/BuscadorItunes/assets/3248076/efabdae9-6e11-4af3-bfdb-cd913becf9af">

[Figma Link](https://www.figma.com/design/WtDm4N04po4gl6RcSVVL2l/30-Vibes?node-id=0-1&t=S2eWnn1qsiTOtQp0-1)

## Features 

- Search a song by name, artist or album
- Play a 30 seconds preview
- Save in favorites
- Save in a playlist
- [_Comming soon_] Dark/Light theme
- [_Comming soon_] Artist's profile screen
- [_Comming soon_] Album's detail screen

## Tech Stats

Built for scalability using best practices in Android development, this app use the following practices:

- Modularized Android application
  - Feature-based modularization and clean architecture
  - UI customization toolkit on commons module
  - Foundation for application data management on core module
  - Custom plugin in buildSrc centralizes and streamlines module build.gradle configurations

- UI Patterns/Concepts
  - Model-View-ViewModel (MVVM) architecture
  - Viewbiding Feature
  - Jetpack Android X Library
  - Theme and styles customizations
  - Material Design based UI/UX
  
- Data Layer
  - Storing local data using Jetpack Room Database
  - Consuming API rest endpoint using Retrofit
  - Integrated with [Itunes API](https://developer.apple.com/library/archive/documentation/AudioVideo/Conceptual/iTuneSearchAPI/index.html#//apple_ref/doc/uid/TP40017632-CH3-SW1)
 
- Some others patterns: 
  - Reactive programming using RxAndroid lib
  - Dependency Injection using Dagger lib
  - Broadcast Receiver + Service to manage music player

- _Commin soon_:
  - Unit Tests + Sonarqube
  - CI/CD Automations
  - Retofit Mock / Wiremock
  - UI Automated Tests
  - Lint

### Guidelines
- Material Design
- Google Example Projects
- Medium Arthicles 
