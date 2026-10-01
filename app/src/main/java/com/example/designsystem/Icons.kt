package com.example.designsystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R

object AppIcons {
    // Official TikTok musical note vector
    val TikTok: ImageVector by lazy {
        ImageVector.Builder(
            name = "TikTok",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Unspecified),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(19.589f, 6.686f)
                curveTo(17.944f, 6.643f, 16.48f, 5.823f, 15.602f, 4.498f)
                curveTo(15.529f, 4.388f, 15.465f, 4.272f, 15.412f, 4.152f)
                horizontalLineTo(12.355f)
                verticalLineTo(15.42f)
                curveTo(12.285f, 17.39f, 10.669f, 18.966f, 8.68f, 18.966f)
                curveTo(6.643f, 18.966f, 4.992f, 17.315f, 4.992f, 15.278f)
                curveTo(4.992f, 13.242f, 6.643f, 11.59f, 8.68f, 11.59f)
                curveTo(9.091f, 11.59f, 9.485f, 11.66f, 9.851f, 11.789f)
                verticalLineTo(8.653f)
                curveTo(9.467f, 8.577f, 9.074f, 8.536f, 8.68f, 8.536f)
                curveTo(4.957f, 8.536f, 1.938f, 11.556f, 1.938f, 15.278f)
                curveTo(1.938f, 19.001f, 4.957f, 22.02f, 8.68f, 22.02f)
                curveTo(12.316f, 22.02f, 15.284f, 19.141f, 15.412f, 15.541f)
                verticalLineTo(9.475f)
                curveTo(16.892f, 10.536f, 18.708f, 11.089f, 20.612f, 11.002f)
                verticalLineTo(7.944f)
                curveTo(20.264f, 7.944f, 19.921f, 7.857f, 19.589f, 7.686f)
                close()
            }
        }.build()
    }
}

/**
 * Top brand badge icon, displaying the 3D AI News icon matching user's design:
 * [ App Icon ] BẢN TIN AI
 */
@Composable
fun AiBrandBadge(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp
) {
    Image(
        painter = painterResource(id = R.drawable.ic_launcher_image),
        contentDescription = "Bản tin AI Icon",
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(8.dp))
    )
}
