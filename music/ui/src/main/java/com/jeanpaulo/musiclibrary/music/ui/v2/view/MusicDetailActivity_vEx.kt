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
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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


class MusicDetailActivity_vEx : BaseMvvmActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CollapsingToolbarTheme {
                CollapsingToolbarParallaxEffect()
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
fun GreetingText(name: String) {
    Text(
        text = "Hello $name",
        modifier = Modifier
            //.height(80.dp).width(240.dp)
            .size(width = 80.dp, height = 80.dp)
            //.fillMaxSize()
            //.fillMaxHeight()
            .clickable { }
            //.padding(start = 20.dp)
            .padding(20.dp),
        //the order is matter
//        style = TextStyle(
//            color = Color.Blue,
//            fontWeight = FontWeight.Bold,
//            fontSize =  18.sp
//        )
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
fun GreetingButton() {
    Button(onClick = {
        // do something
    }) {
        GreetingText(name = "button")
        GreetingText(name = "button")
    }

}

@Composable
fun MainScreenVEx() {
    Surface(
        color = Color.DarkGray,
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            color = Color.Magenta,
            modifier = Modifier
                .wrapContentSize(align = Alignment.CenterEnd)
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    //horizontalArrangement = Arrangement.SpaceAround,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    //horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalColoredBar(Color.Blue)
                    HorizontalColoredBar(Color.White)
                    HorizontalColoredBar(Color.Blue)
                    HorizontalColoredBar(Color.White)
                    HorizontalColoredBar(Color.Blue)
                    HorizontalColoredBar(Color.White)
                    HorizontalColoredBar(Color.Yellow)
                }
                Text(
                    text = "wrapped content",
                    modifier = Modifier.wrapContentSize(
                        align = Alignment.BottomStart
                    ),
                    style = MaterialTheme.typography.h3
                )
                Text(
                    text = "wrapped content",
                    modifier = Modifier.wrapContentSize(
                        align = Alignment.BottomStart
                    ),
                    style = MaterialTheme.typography.h3
                )
            }
        }
    }
}

@Composable
fun HorizontalColoredBar(color: Color) {
    Surface(
        color = color,
        modifier = Modifier
            .height(40.dp)
            .width(40.dp)
    ) {}
}

@Composable
fun MusicDetailActivitySurface() {
    Scaffold(
        topBar = {
            TopBar(currentHeight = 128, title = "Jean paulo de Almeida", onBack = { /*TODO*/ }) {

            }
        },
        content = { padding ->
            Column(modifier = Modifier.padding(padding)) {
                Box(Modifier.fillMaxSize()) {
                    Text(
                        text = "Contenido",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    )
}

// CollapsingToolbar
// Ref: https://proandroiddev.com/collapsing-toolbar-with-parallax-effect-and-curve-motion-in-jetpack-compose-9ed1c3c0393f

private val headerHeight = 250.dp
private val toolbarHeight = 56.dp

private val paddingMedium = 16.dp

private val titlePaddingStart = 16.dp
private val titlePaddingEnd = 72.dp

private const val titleFontScaleStart = 1f
private const val titleFontScaleEnd = 0.66f


@Composable
fun Screen() {
    val scroll: ScrollState = rememberScrollState(0)

    val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.toPx() }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Header(scroll, headerHeightPx)
        Body(scroll)
        Toolbar(scroll, headerHeightPx, toolbarHeightPx)
        Title(scroll, headerHeightPx, toolbarHeightPx)
    }
}

@Composable
fun CollapsingToolbarParallaxEffect() {
    val scroll: ScrollState = rememberScrollState(0)

    val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.toPx() }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Header(scroll, headerHeightPx)
        Body(scroll)
        Toolbar(scroll, headerHeightPx, toolbarHeightPx)
        Title(scroll, headerHeightPx, toolbarHeightPx)
    }
}

@Composable
private fun Header(scroll: ScrollState, headerHeightPx: Float) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(headerHeight)
        .graphicsLayer {
            translationY = -scroll.value.toFloat() / 2f // Parallax effect
            alpha = (-1f / headerHeightPx) * scroll.value + 1
        }
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_pexel),
            contentDescription = "",
            contentScale = ContentScale.FillBounds
        )

        Box(
            Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.White),
                        startY = 3 * headerHeightPx / 4 // Gradient applied to wrap the title only
                    )
                )
        )
    }
}

@Composable
private fun Body(scroll: ScrollState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .verticalScroll(scroll)
    ) {
        Spacer(Modifier.height(headerHeight))
        repeat(5) {
            Text(
                text = stringResource(R.string.lorem_ipsum),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    //.background(Color(0XFF161616))
                    .padding(16.dp)
            )
        }
    }
}

@Composable
private fun Toolbar(scroll: ScrollState, headerHeightPx: Float, toolbarHeightPx: Float) {
    val toolbarBottom = headerHeightPx - toolbarHeightPx
    val showToolbar by remember {
        derivedStateOf {
            scroll.value >= toolbarBottom
        }
    }

    AnimatedVisibility(
        visible = showToolbar,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300))
    ) {
        TopAppBar(
            modifier = Modifier.background(
                brush = Brush.horizontalGradient(
                    listOf(Color(0xff026586), Color(0xff032C45))
                )
            ),
            navigationIcon = {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .padding(16.dp)
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            },
            title = {},
            backgroundColor = Color.Transparent,
            elevation = 0.dp
        )
    }
}

@Composable
private fun Title(
    scroll: ScrollState,
    headerHeightPx: Float,
    toolbarHeightPx: Float
) {
    var titleHeightPx by remember { mutableStateOf(0f) }
    var titleWidthPx by remember { mutableStateOf(0f) }

    Text(
        text = "New York",
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .graphicsLayer {
                val collapseRange: Float = (headerHeightPx - toolbarHeightPx)
                val collapseFraction: Float = (scroll.value / collapseRange).coerceIn(0f, 1f)

                val scaleXY = lerp(
                    titleFontScaleStart.dp,
                    titleFontScaleEnd.dp,
                    collapseFraction
                )

                val titleExtraStartPadding = titleWidthPx.toDp() * (1 - scaleXY.value) / 2f

                val titleYFirstInterpolatedPoint = lerp(
                    headerHeight - titleHeightPx.toDp() - paddingMedium,
                    headerHeight / 2,
                    collapseFraction
                )

                val titleXFirstInterpolatedPoint = lerp(
                    titlePaddingStart,
                    (titlePaddingEnd - titleExtraStartPadding) * 5 / 4,
                    collapseFraction
                )

                val titleYSecondInterpolatedPoint = lerp(
                    headerHeight / 2,
                    toolbarHeight / 2 - titleHeightPx.toDp() / 2,
                    collapseFraction
                )

                val titleXSecondInterpolatedPoint = lerp(
                    (titlePaddingEnd - titleExtraStartPadding) * 5 / 4,
                    titlePaddingEnd - titleExtraStartPadding,
                    collapseFraction
                )

                val titleY = lerp(
                    titleYFirstInterpolatedPoint,
                    titleYSecondInterpolatedPoint,
                    collapseFraction
                )

                val titleX = lerp(
                    titleXFirstInterpolatedPoint,
                    titleXSecondInterpolatedPoint,
                    collapseFraction
                )

                translationY = titleY.toPx()
                translationX = titleX.toPx()
                scaleX = scaleXY.value
                scaleY = scaleXY.value
            }
            .onGloballyPositioned {
                titleHeightPx = it.size.height.toFloat()
                titleWidthPx = it.size.width.toFloat()
            }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreviewVEx() {
    CollapsingToolbarTheme {
        MainScreenVEx()
    }
}


