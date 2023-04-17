package com.jeanpaulo.musiclibrary.music.ui.v2.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmActivity
import com.jeanpaulo.musiclibrary.core.presentation.SimpleMusicDetailUIModel
import com.jeanpaulo.musiclibrary.music.ui.R
import com.jeanpaulo.musiclibrary.music.ui.v2.theme.*


class MusicDetailActivity : BaseMvvmActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CollapsingToolbarTheme {
                MainScreen()
            }
        }
    }

    companion object {

        const val VIEW_NAME_HEADER_IMAGE = "detail:header:image"
        const val VIEW_NAME_HEADER_TITLE = "detail:header:title"

        const val MUSIC_PARAM = "music_param"
        const val FROM_REMOTE_PARAM = "from_remote_param"

        fun newInstance(
            context: Context,
            music: SimpleMusicDetailUIModel,
            fromRemote: Boolean
        ): Intent {
            return Intent(context, MusicDetailActivity::class.java).apply {
                putExtra(MUSIC_PARAM, music)
                putExtra(FROM_REMOTE_PARAM, fromRemote)
            }
        }
    }
}

@Composable
fun TopAppBar() {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Sharp.ArrowBack, contentDescription = "Voltar")
            }
        },
        title = {
            Text(
                text = "90`s Old School ",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(imageVector = Icons.Filled.Star, contentDescription = "Favorito")
            }
        },
        modifier = Modifier.background(Color.White)
    )
}

@Composable
fun Artwork(modifier: Modifier) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 120.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        Card(
            modifier = modifier
                .size(280.dp)
                .padding(10.dp),
            shape = RoundedCornerShape(corner = CornerSize(24.dp)),
            elevation = 1.dp
        ) {
            Image(
                painterResource(R.drawable.bg_pexel),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Text(
            text = "Sky`s The Limit (feat. 112)",
            modifier = Modifier
                .wrapContentWidth()
                .padding(top = 20.dp),
            style = MaterialTheme.typography.h6
        )

        Text(
            text = "Notourious Big",
            modifier = Modifier.wrapContentWidth(),
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun Controller(modifier: Modifier) {
    Row(
        modifier = modifier
            .padding(bottom = 30.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .padding(12.dp)
                .size(32.dp),
            shape = CircleShape,
            elevation = 1.dp
        ) {
            Image(
                imageVector = Icons.Default.Repeat,
                contentDescription = "Repeat",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }

        Card(
            modifier = Modifier
                .size(96.dp),
            shape = CircleShape,
            elevation = 1.dp
        ) {
            Image(
                imageVector = Icons.Default.PlayCircle,
                contentDescription = "Play",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}


@Composable
fun MainScreen() {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar() },
        bottomBar = { },
        backgroundColor = Color.White
    ) { padding ->
        Box(
            Modifier
                .background(Color.Green)
                .fillMaxSize()
        ) {
            Artwork(modifier = Modifier.align(TopCenter))
            Controller(modifier = Modifier.align(BottomCenter))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainScreen()
}


