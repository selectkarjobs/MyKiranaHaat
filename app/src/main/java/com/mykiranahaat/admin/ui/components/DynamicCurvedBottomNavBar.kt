package com.mykiranahaat.admin.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DynamicCurvedBottomNavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0xFF1976D2), // Vibrant Blue
    fabColor: Color = Color(0xFF9C27B0), // Purple
    fabSize: Dp = 64.dp
) {
    val navBarHeight = 80.dp
    val curveWidth = fabSize * 1.2f
    val curveDepth = fabSize / 2.2f
    val iconSize = 28.dp
    val iconSelectedSize = 34.dp
    val itemCount = curvedNavItems.size

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenWidthPx = with(density) { configuration.screenWidthDp.dp.toPx() }
    val tabWidthPx = screenWidthPx / itemCount

    // Animated tab center for curve/fab
    val targetTabCenter = (selectedIndex + 0.5f) * (screenWidthPx / itemCount)
    val animatedTabCenter by animateFloatAsState(
        targetValue = targetTabCenter,
        animationSpec = tween(durationMillis = 350)
    )

    // Animated color for background if you want dynamic color transitions
    val animatedContainerColor by animateColorAsState(
        targetValue = containerColor,
        animationSpec = tween(durationMillis = 350)
    )

    // The fix: Use only .fillMaxWidth() and .height() for the container, no vertical padding, no wrapContentHeight, no navigationBarsPadding.
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(navBarHeight + fabSize / 2)
            .background(Color.Transparent),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Curved background with animated cutout under selected tab
        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(navBarHeight + fabSize / 2)
        ) {
            val width = size.width
            val height = navBarHeight.toPx()
            val fabRadius = fabSize.toPx() / 2f
            val curveRadius = curveWidth.toPx() / 2f

            // Animate the curve center
            val tabCenter = animatedTabCenter

            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(tabCenter - curveRadius - 18, 0f)
                cubicTo(
                    tabCenter - curveRadius, 0f,
                    tabCenter - fabRadius * 1.2f, curveDepth.toPx(),
                    tabCenter, curveDepth.toPx()
                )
                cubicTo(
                    tabCenter + fabRadius * 1.2f, curveDepth.toPx(),
                    tabCenter + curveRadius, 0f,
                    tabCenter + curveRadius + 18, 0f
                )
                lineTo(width, 0f)
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }
            drawIntoCanvas {
                it.drawPath(path, androidx.compose.ui.graphics.Paint().apply { color = animatedContainerColor })
            }
        }

        // Navigation items: selected tab as FAB, others normal
        Row(
            Modifier
                .fillMaxWidth()
                .height(navBarHeight)
                .padding(horizontal = 8.dp)
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            curvedNavItems.forEachIndexed { idx, item ->
                val isSelected = idx == selectedIndex
                val animatedIconSize by animateDpAsState(
                    targetValue = if (isSelected) iconSelectedSize else iconSize,
                    animationSpec = tween(durationMillis = 350)
                )
                val animatedIconColor by animateColorAsState(
                    targetValue = if (isSelected) fabColor else Color.White,
                    animationSpec = tween(durationMillis = 350)
                )
                if (isSelected) {
                    // The selected tab as FAB, offset upwards
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(fabSize)
                            .offset(y = (-fabSize / 1.35f))
                            .clip(CircleShape)
                            .background(fabColor)
                            .clickable { onItemSelected(idx) }
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = Color.White,
                            modifier = Modifier.size(fabSize * 0.5f)
                        )
                    }
                } else {
                    // Normal tab with icon and text
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onItemSelected(idx) }
                            .padding(bottom = 8.dp)
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = animatedIconColor,
                            modifier = Modifier.size(animatedIconSize)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.label,
                            color = Color.White,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Selected tab label below FAB, centered under FAB's slot
        val selectedTab = curvedNavItems[selectedIndex]
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(navBarHeight + fabSize / 2),
            contentAlignment = Alignment.BottomStart
        ) {
            Box(
                modifier = Modifier
                    .width(with(LocalDensity.current) { tabWidthPx.toDp() })
                    .offset(x = with(LocalDensity.current) { (selectedIndex * tabWidthPx).toDp() }),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.offset(y = (-fabSize / 6))
                ) {
                    Spacer(modifier = Modifier.height(fabSize / 2 + 6.dp))
                    Text(
                        text = selectedTab.label,
                        color = fabColor,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}