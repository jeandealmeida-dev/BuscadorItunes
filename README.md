# 30 Vibes

This is an Android Project that implements an Music library. It can look up for a song by the name than if you press an item list it shows the music detail. Also you can reproduce the music`s preview. 

## Features

<table>
    <thead>
        <tr>
            <th>Screenshot</th>
            <th>Details</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td align="center"><img src="https://github.com/user-attachments/assets/2ef46802-cb7a-4126-8280-62081ba0ec16"></td>
            <td>
              <ul>
                <h3>Search and Reproduce</h3>
                <li>Search music by track name, artist or collection</li>
                <li>Reproduce the music's preview</li>
                <li>Save a song as favorite</li>
                <li><i>Comming soon..</i> Add a song in a playlist</li>
                <li><i>Comming soon..</i> Open album details</li>
                <li><i>Comming soon..</i> Open artist details</li>
              </ul>
            </td>
        </tr>
        <tr>
            <td align="center"><img src="https://github.com/user-attachments/assets/514b22ec-7699-4ed3-9f6f-8defb240a798"></td>
            <td>
              <ul>
                <h3>Playlist and Favorites</h3>
                <li>Create, edit and delete playlists</li>
                <li>Reproduce the music's preview</li>
                <li>Reproduce a favorite list</li>
                <li>Manage favorite playlist</li>
                <li><i>Comming soon..</i> Reproduce a playlist music list</li>
              </ul>
            </td>
        </tr>
       <tr>
            <td align="center"><img src="https://github.com/user-attachments/assets/12479845-f66d-4dd7-974c-819776bb8a9a"></td>
            <td>
              <ul>
                <h3>Settings</h3>
                <li>Change Light/Dark theme</li>
                <li>Drop all database</li>
                <li>Send us a feedback</li>
              </ul>
            </td>
        </tr>
    </tbody>
</table>

## Inspiration & Design

<img width="756" alt="image" src="https://github.com/jeandealmeida-dev/BuscadorItunes/assets/3248076/efabdae9-6e11-4af3-bfdb-cd913becf9af">

[Figma Link](https://www.figma.com/design/WtDm4N04po4gl6RcSVVL2l/30-Vibes?node-id=0-1&t=S2eWnn1qsiTOtQp0-1)

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
