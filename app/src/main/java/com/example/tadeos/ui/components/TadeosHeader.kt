package com.example.tadeos.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tadeos.R
import com.example.tadeos.ui.theme.TerracottaClay

@Composable
fun TadeosBrandHeader(
    onBrandClick: () -> Unit,
    modifier: Modifier = Modifier,
    logoSize: Dp = 36.dp,
    bellSize: Dp = 26.dp,
    titleSize: TextUnit = 16.sp,
    titleLineHeight: TextUnit = 20.sp
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.clickable(onClick = onBrandClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_tadeos),
                contentDescription = "Perfil de Tadeo's",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(logoSize)
                    .clip(CircleShape)
            )
            Text(
                text = "Tadeo's",
                modifier = Modifier.padding(start = 10.dp),
                color = TerracottaClay,
                fontSize = titleSize,
                lineHeight = titleLineHeight,
                fontWeight = FontWeight.Bold
            )
        }

        TadeosNotificationBell(
            color = TerracottaClay,
            modifier = Modifier.size(bellSize)
        )
    }
}

@Composable
private fun TadeosNotificationBell(
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val stroke = Stroke(
            width = 1.7.dp.toPx(),
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
        val bell = Path().apply {
            moveTo(size.width * 0.34f, size.height * 0.68f)
            lineTo(size.width * 0.66f, size.height * 0.68f)
            lineTo(size.width * 0.62f, size.height * 0.39f)
            cubicTo(
                size.width * 0.60f,
                size.height * 0.25f,
                size.width * 0.40f,
                size.height * 0.25f,
                size.width * 0.38f,
                size.height * 0.39f
            )
            close()
        }

        drawPath(path = bell, color = color, style = stroke)
        drawLine(
            color = color,
            start = Offset(size.width * 0.30f, size.height * 0.68f),
            end = Offset(size.width * 0.70f, size.height * 0.68f),
            strokeWidth = 1.7.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawCircle(
            color = color,
            radius = size.minDimension * 0.04f,
            center = Offset(size.width * 0.50f, size.height * 0.76f)
        )
    }
}
