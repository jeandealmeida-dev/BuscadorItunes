package com.jeanpaulo.musiclibrary.app.ui.components

import android.content.res.Configuration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.jeanpaulo.musiclibrary.app.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.jeanpaulo.musiclibrary.app.ui.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name), fontSize = MaterialTheme.typography.titleLarge.fontSize) },
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.primary,
            actionIconContentColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.primary,
        )
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun TopBarPreview_DarkTheme() {
    AppTheme {
        TopBar()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun TopBarPreview_LightTheme() {
    AppTheme {
        TopBar()
    }
}