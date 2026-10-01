package com.example.library

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Audiotrack
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.designsystem.*
import com.example.model.NewsProject
import com.example.model.SceneItem
import com.example.ui.MainViewModel

@Composable
fun LibraryScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val projects by viewModel.projects.collectAsState()
    val selectedProject = viewModel.selectedProject.collectAsState().value ?: projects.firstOrNull()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Hình ảnh (6)", "Audio", "Bài viết")

    var previewScene by remember { mutableStateOf<SceneItem?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        // Screen Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Thư viện tài nguyên",
                    style = TypographyH1.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                if (selectedProject != null) {
                    Text(
                        text = "Project: ${selectedProject.id}",
                        style = TypographyCaption.copy(fontWeight = FontWeight.Medium),
                        color = PrimaryBlue
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = SurfaceWhite,
                border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Folder,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "4 Dự án",
                        style = TypographyCaption.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                    )
                }
            }
        }

        // Filter Tabs
        FilterChipBar(
            tabs = tabs,
            selectedIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Content based on tab
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            val scenes = selectedProject?.scenes ?: emptyList()

            when (selectedTabIndex) {
                0 -> {
                    // Images Grid (2 columns x 3 rows)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(scenes.size) { index ->
                            val scene = scenes[index]
                            ImageCard(
                                scene = scene,
                                onClick = { previewScene = scene }
                            )
                        }
                    }
                }
                1 -> {
                    // Audio List (001.mp3 to 006.mp3)
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(scenes.size) { index ->
                            val scene = scenes[index]
                            AudioTrackCard(
                                scene = scene,
                                onPlay = {
                                    viewModel.previewVoice(
                                        com.example.model.TTSVoice(
                                            id = "voice_1",
                                            name = "Nam Bắc 01",
                                            gender = "Nam",
                                            region = "Bắc",
                                            sampleText = scene.text
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
                2 -> {
                    // Articles / Script scenes list
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(scenes.size) { index ->
                            val scene = scenes[index]
                            ArticleSceneCard(scene = scene)
                        }
                    }
                }
            }
        }
    }

    if (previewScene != null) {
        val sc = previewScene!!
        AlertDialog(
            onDismissRequest = { previewScene = null },
            title = {
                Text(text = "Hình ảnh: ${sc.imageFileName}", style = TypographyH2)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(sc.visualPlaceholderColor),
                                        Color(0xFF0F172A)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "[ 9:16 Rendered Asset ]\n${sc.imageFileName}",
                            color = Color.White,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            style = TypographyCaption
                        )
                    }

                    Text("Prompt Imagen:", style = TypographyCaption.copy(fontWeight = FontWeight.Bold))
                    Text(sc.imagePrompt, style = TypographyBodySmall, color = TextSecondary)

                    Text("Lời thoại đi kèm:", style = TypographyCaption.copy(fontWeight = FontWeight.Bold))
                    Text(sc.text, style = TypographyBodySmall)
                }
            },
            confirmButton = {
                TextButton(onClick = { previewScene = null }) {
                    Text("Đóng", color = PrimaryBlue)
                }
            }
        )
    }
}

@Composable
private fun ImageCard(
    scene: SceneItem,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("library_image_${scene.imageFileName}"),
        color = SurfaceWhite,
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(scene.visualPlaceholderColor),
                                Color(0xFF0F172A)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Futuristic thumbnail decorative illustration
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Image,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Scene #${scene.sceneNumber}",
                        style = TypographyCaption.copy(color = Color.White.copy(alpha = 0.8f))
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = scene.imageFileName,
                    style = TypographyCaption.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                )
            }
        }
    }
}

@Composable
private fun AudioTrackCard(
    scene: SceneItem,
    onPlay: () -> Unit
) {
    AppCard(
        contentPadding = PaddingValues(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(PurpleAccentContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Audiotrack,
                        contentDescription = null,
                        tint = PurpleAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = scene.audioFileName,
                        style = TypographyH3.copy(fontWeight = FontWeight.SemiBold),
                        color = TextPrimary
                    )
                    Text(
                        text = "00:${scene.durationSeconds} • Giọng Nam Bắc 01",
                        style = TypographyBodySmall,
                        color = TextSecondary
                    )
                }
            }

            Surface(
                shape = CircleShape,
                color = PrimaryBlue,
                modifier = Modifier
                    .clickable { onPlay() }
                    .testTag("play_audio_${scene.audioFileName}")
            ) {
                Box(
                    modifier = Modifier.size(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PlayArrow,
                        contentDescription = "Phát",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ArticleSceneCard(scene: SceneItem) {
    AppCard(contentPadding = PaddingValues(12.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Cảnh #${scene.sceneNumber}",
                    style = TypographyH3.copy(fontWeight = FontWeight.Bold),
                    color = PrimaryBlue
                )
                Text(
                    text = "Thời lượng: ${scene.durationSeconds}s",
                    style = TypographyCaption,
                    color = TextSecondary
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = scene.text,
                style = TypographyBody,
                color = TextPrimary
            )
        }
    }
}
