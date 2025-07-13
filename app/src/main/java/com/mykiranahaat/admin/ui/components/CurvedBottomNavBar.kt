package com.mykiranahaat.admin.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun CurvedBottomNavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
    fabColor: Color = MaterialTheme.colorScheme.primary,
    fabSize: Dp = 64.dp
) {
    val navBarHeight = 80.dp
    val fabElevation = 8.dp
    val curveWidth = fabSize * 1.2f
    val curveDepth = fabSize / 2.2f
    val iconSize = 28.dp
    val iconSelectedSize = 34.dp
    val centerIndex = 2 // Dashboard is the center tab

    Box(
        modifier
            .fillMaxWidth()
            .height(navBarHeight + fabSize / 2)
            .wrapContentHeight(Alignment.Bottom),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Draw curved background
        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(navBarHeight + fabSize / 2)
        ) {
            val width = size.width
            val height = navBarHeight.toPx()
            val fabRadius = fabSize.toPx() / 2f
            val curveRadius = curveWidth.toPx() / 2f
            val curveCenter = width / 2f

            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(curveCenter - curveRadius - 18, 0f)
                cubicTo(
                    curveCenter - curveRadius, 0f,
                    curveCenter - fabRadius * 1.2f, curveDepth.toPx(),
                    curveCenter, curveDepth.toPx()
                )
                cubicTo(
                    curveCenter + fabRadius * 1.2f, curveDepth.toPx(),
                    curveCenter + curveRadius, 0f,
                    curveCenter + curveRadius + 18, 0f
                )
                lineTo(width, 0f)
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }
            drawIntoCanvas {
                it.drawPath(path, androidx.compose.ui.graphics.Paint().apply { color = containerColor })
            }
        }

        // Navigation items (center is FAB)
        Row(
            Modifier
                .fillMaxWidth()
                .height(navBarHeight)
                .padding(horizontal = 8.dp, vertical = 0.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            curvedNavItems.forEachIndexed { idx, item ->
                val isSelected = idx == selectedIndex
                if (idx == centerIndex) {
                    // Center tab (FAB look), moved further above so label is not overlapped
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(fabSize)
                            .offset(y = (-fabSize / 1.35f)) // Increased offset upwards
                            .clip(CircleShape)
                            .background(fabColor)
                            .shadow(elevation = fabElevation, shape = CircleShape, clip = false)
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
                    // Normal tabs
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
                            tint = if (isSelected) fabColor else Color.Gray,
                            modifier = Modifier.size(if (isSelected) iconSelectedSize else iconSize)
                        )
                        Text(
                            text = item.label,
                            color = if (isSelected) fabColor else Color.Gray,
                            fontSize = if (isSelected) 13.sp else 11.sp
                        )
                    }
                }
            }
        }

        // Dashboard label below FAB, centered
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(navBarHeight + fabSize / 2),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .offset(y = (-fabSize / 6))
            ) {
                Spacer(modifier = Modifier.height(fabSize / 2 + 6.dp))
                Text(
                    text = curvedNavItems[centerIndex].label,
                    color = if (selectedIndex == centerIndex) fabColor else Color.Gray,
                    fontSize = if (selectedIndex == centerIndex) 13.sp else 11.sp
                )
            }
        }
    }
}