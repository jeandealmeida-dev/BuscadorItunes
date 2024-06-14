package com.jeanpaulo.musiclibrary.app.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : NavigationItem("home", Icons.Default.Home, "Home")
    object Search : NavigationItem("search", Icons.Default.Search, "Search")
    object Playlist : NavigationItem("playlists", Icons.Default.Favorite, "Playlists")
    object Config : NavigationItem("config", Icons.Default.Build, "Config")
}