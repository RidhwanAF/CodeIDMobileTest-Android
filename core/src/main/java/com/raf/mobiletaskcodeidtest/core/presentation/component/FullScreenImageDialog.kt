package com.raf.mobiletaskcodeidtest.core.presentation.component

import android.graphics.Bitmap
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.FullScreenImageDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onDismissRequest: () -> Unit,
    bitmap: Bitmap,
    label: String,
) {
    var backProgress by remember {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(visible) {
        if (visible) {
            backProgress = 0f
        }
    }

    PredictiveBackHandler(visible) { backEvent ->
        backEvent.collect { event ->
            backProgress = event.progress
        }
        onDismissRequest()
        backProgress = 0f
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        var scale by remember { mutableFloatStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = (1.0f - backProgress).coerceAtLeast(0.25f)))
        ) {
            val state = rememberTransformableState { zoomChange, offsetChange, _ ->
                scale = (scale * zoomChange).coerceIn(-0.985f, 5f)
                offset += offsetChange
            }
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = label,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState("full-screen-image-dialog-${label}"),
                        animatedVisibilityScope = this@AnimatedVisibility,
                        clipInOverlayDuringTransition = OverlayClip(CircleShape)
                    )
                    .scale((1f - backProgress).coerceAtLeast(0.85f))
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    .transformable(state = state)
            )
            FilledTonalIconButton(
                onClick = {
                    onDismissRequest.invoke()
                },
                modifier = Modifier
                    .padding(16.dp)
                    .systemBarsPadding()
                    .align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        }
    }
}