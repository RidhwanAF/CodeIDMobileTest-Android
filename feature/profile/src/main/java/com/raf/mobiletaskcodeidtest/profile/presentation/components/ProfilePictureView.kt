package com.raf.mobiletaskcodeidtest.profile.presentation.components

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ProfilePictureView(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    label: String,
    bitmap: Bitmap?,
    onEditClick: () -> Unit,
    onClick: () -> Unit,
) {
    val context = LocalContext.current
    val initialLabel = label.split(" ")
        .takeIf { it.isNotEmpty() }
        ?.joinToString("") { it.firstOrNull()?.toString() ?: "" }?.take(3)
        ?: ""

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(125.dp)
                .clip(CircleShape)
                .aspectRatio(1f)
                .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .clickable {
                    if (bitmap == null) {
                        Toast.makeText(context, "No profile picture found", Toast.LENGTH_SHORT)
                            .show()
                        return@clickable
                    }
                    onClick()
                }
                .align(Alignment.Center)
        ) {
            if (bitmap != null) {
                AnimatedVisibility(visible = visible) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = label,
                        modifier = Modifier
                            .sharedElement(
                                sharedContentState = rememberSharedContentState("full-screen-image-dialog-${label}"),
                                animatedVisibilityScope = this@AnimatedVisibility
                            )
                            .fillMaxSize()
                    )
                }
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.onPrimary)
                        .fillMaxSize()
                ) {
                    Text(
                        text = initialLabel.uppercase(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 32.sp,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        FilledIconButton(
            onClick = onEditClick,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit profile picture"
            )
        }
    }
}