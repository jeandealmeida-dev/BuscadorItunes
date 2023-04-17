package com.jeanpaulo.musiclibrary.music.ui.v2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.jeanpaulo.musiclibrary.music.ui.v2.view.CollapsingTopBar.BACK_ID
import com.jeanpaulo.musiclibrary.music.ui.v2.view.CollapsingTopBar.SHARE_ID
import com.jeanpaulo.musiclibrary.music.ui.v2.view.CollapsingTopBar.TITLE_ID
import kotlin.math.roundToInt
import com.jeanpaulo.musiclibrary.music.ui.R
import com.jeanpaulo.musiclibrary.music.ui.v2.view.CollapsingTopBar.COLLAPSE_FACTOR

@Composable
fun CollapsingTopBar(
    modifier: Modifier = Modifier,
    collapseFactor: Float = 1f, // A value from (0-1) where 0 means fully expanded
    content: @Composable () -> Unit
) {
    val map = mutableMapOf<Placeable, Int>()
    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->
        map.clear()
        val placeables = mutableListOf<Placeable>()
        measurables.map { measurable ->
            when (measurable.layoutId) {
                BACK_ID -> measurable.measure(constraints)
                SHARE_ID -> measurable.measure(constraints)
                TITLE_ID ->
                    measurable.measure(
                        Constraints.fixedWidth(
                            constraints.maxWidth
                                    - (collapseFactor * (placeables.first().width * 2)).toInt()
                        )
                    )
                else -> throw IllegalStateException("Id Not found")
            }.also { placeable ->
                map[placeable] = measurable.layoutId as Int
                placeables.add(placeable)
            }
        }

        // Set the size of the layout as big as it can
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEach { placeable ->
                when (map[placeable]) {
                    BACK_ID -> placeable.placeRelative(0, 0)
                    SHARE_ID -> placeable.run {
                        placeRelative(constraints.maxWidth - width, 0)
                    }
                    TITLE_ID -> placeable.run {
                        val widthOffset = (placeables[0].width * collapseFactor).roundToInt()
                        val heightOffset = (placeables.first().height - placeable.height) / 2
                        placeRelative(
                            widthOffset,
                            if (collapseFactor == 1f) heightOffset else constraints.maxHeight - height
                        )
                    }
                }
            }
        }
    }
}

object CollapsingTopBar {
    const val BACK_ID = 1001
    const val SHARE_ID = 1002
    const val TITLE_ID = 1003
    const val COLLAPSE_FACTOR = 0.6f
}

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    currentHeight: Int,
    title: String,
    onBack: () -> Unit,
    shareShow: () -> Unit
) {

    Box(
        modifier = modifier
            .height(currentHeight.dp),
    ) {
        CollapsingTopBar(
            collapseFactor = COLLAPSE_FACTOR,// calculate collapseFactor based on max and min height of the toolbar,
            modifier = Modifier
                .background(Color.DarkGray)
                .statusBarsPadding(),
        ) {
            IconButton(
                modifier = Modifier
                    .wrapContentWidth()
                    .layoutId(CollapsingTopBar.BACK_ID)
                    .padding(16.dp),
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.action_favorite),
                    tint = MaterialTheme.colors.onPrimary,
                )
            }
            IconButton(
                modifier = Modifier
                    .wrapContentSize()
                    .layoutId(CollapsingTopBar.SHARE_ID)
                    .padding(16.dp),
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = stringResource(id = R.string.action_settings),
                    tint = MaterialTheme.colors.onPrimary,
                )
            }
            Text(
                modifier = Modifier
                    .layoutId(CollapsingTopBar.TITLE_ID)
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp),
                text = title,
                style = MaterialTheme.typography.h4.copy(color = MaterialTheme.colors.onPrimary),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}