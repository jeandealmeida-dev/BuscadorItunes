package com.jeanpaulo.musiclibrary.app.ui.components

import android.content.res.Configuration
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jeanpaulo.musiclibrary.app.ui.AppTheme
import com.jeanpaulo.musiclibrary.app.view.ConfigScreen
import com.jeanpaulo.musiclibrary.app.view.HomeScreen
import com.jeanpaulo.musiclibrary.app.view.SearchScreen

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Search,
        NavigationItem.Playlist,
        NavigationItem.Config,
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(text = item.label) },
                colors = NavigationBarItemColors(
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    selectedIndicatorColor = MaterialTheme.colorScheme.primary,
                    selectedIconColor = MaterialTheme.colorScheme.background,
                    unselectedTextColor = MaterialTheme.colorScheme.secondary,
                    unselectedIconColor = MaterialTheme.colorScheme.secondary,
                    disabledIconColor = MaterialTheme.colorScheme.tertiary,
                    disabledTextColor = MaterialTheme.colorScheme.tertiary,
                ),
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.startDestinationId)
                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BottomNavigationBarPreview_DarkTheme() {
    AppTheme {
        val navController = rememberNavController()
        BottomNavigationBar(navController)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun BottomNavigationBarPreview_LightTheme() {
    AppTheme {
        val navController = rememberNavController()
        BottomNavigationBar(navController)
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            HomeScreen()
        }
        composable(NavigationItem.Search.route) {
            SearchScreen()
        }
        composable(NavigationItem.Config.route) {
            ConfigScreen()
        }
        composable(NavigationItem.Playlist.route) {
            ConfigScreen()
        }
    }
}