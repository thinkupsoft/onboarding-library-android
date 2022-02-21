package com.thinkup.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val count = 4
private var added = 0

private val positions = arrayOfNulls<Rect>(count)

private fun addPosition(item: Rect, i: Int) {
    if (positions[i] == null) {
        positions[i] = item
        added++
        if (count == added) onPositionsReady(positions.toList() as List<Rect>)
    }
}

private lateinit var onPositionsReady: (List<Rect>) -> Unit

@Composable
fun Home(onPositionsReadyCallback: (List<Rect>) -> Unit) {
    onPositionsReady = onPositionsReadyCallback
    val allTasks = arrayOf("Buy Milk", "Choose curtain")
    val allTopics = listOf(
        "2 new packages arrived",
        "DIY project recommendation",
        "Festival next month",
        "New flower seeds available",
    )

    // True if the whether data is currently loading.
    var weatherLoading by remember { mutableStateOf(false) }

    // Holds all the tasks currently shown on the task list.
    val tasks = remember { mutableStateListOf(*allTasks) }

    // Holds the topic that is currently expanded to show its body.
    var expandedTopic by remember { mutableStateOf<String?>(null) }

    // True if the message about the edit feature is shown.
    var editMessageShown by remember { mutableStateOf(false) }

    // Simulates loading weather data. This takes 3 seconds.
    suspend fun loadWeather() {
        if (!weatherLoading) {
            weatherLoading = true
            delay(3000L)
            weatherLoading = false
        }
    }

    // Shows the message about edit feature.
    suspend fun showEditMessage() {
        if (!editMessageShown) {
            editMessageShown = true
            delay(3000L)
            editMessageShown = false
        }
    }

    // Load the weather at the initial composition.
    LaunchedEffect(Unit) {
        loadWeather()
    }

    val lazyListState = rememberLazyListState()

    // The coroutine scope for event handlers calling suspend functions.
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        backgroundColor = Color(0xFFE1BEE7),
        floatingActionButton = {
            HomeFloatingActionButton(
                onClick = {
                    coroutineScope.launch {
                        showEditMessage()
                    }
                },
            )
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 32.dp),
            state = lazyListState,

            ) {
            // Weather
            item {
                Column(modifier = Modifier.onGloballyPositioned {
                    addPosition(it.boundsInRoot(), 0)
                }) {
                    Header(title = "Weather")
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 2.dp
                    ) {
                        WeatherRow(onRefresh = {
                            coroutineScope.launch {
                                loadWeather()
                            }
                        })
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Topics
            item { Header(title = "Topics") }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            items(allTopics) { topic ->
                TopicRow(
                    topic = topic,
                    expanded = expandedTopic == topic,
                    onClick = {
                        expandedTopic = if (expandedTopic == topic) null else topic
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }

            // Tasks
            item { Header(title = "Tasks") }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            if (tasks.isEmpty()) {
                item {
                    TextButton(onClick = { tasks.clear(); tasks.addAll(allTasks) }) {}
                }
            }
            items(count = tasks.size) { i ->
                val task = tasks.getOrNull(i)
                if (task != null) {
                    key(task) {
                        TaskRow(
                            task = task
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeatherRow(
    onRefresh: () -> Unit
) {
    Row(
        modifier = Modifier
            .heightIn(min = 64.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFFFFB300))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = "18 ℃", fontSize = 24.sp)
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onRefresh, modifier = Modifier.onGloballyPositioned {
            addPosition(it.boundsInRoot(), 1)
            addPosition(it.boundsInRoot(), 3)
        }) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = ""
            )
        }
    }
}

@Composable
private fun TaskRow(task: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = task,
                style = MaterialTheme.typography.body1
            )
        }
    }
}


/**
 * Shows the header label.
 *
 * @param title The title to be shown.
 */
@Composable
private fun Header(
    title: String
) {
    Text(
        text = title,
        modifier = Modifier
            .semantics { heading() },
        style = MaterialTheme.typography.h5
    )
}

/**
 * Shows a row for one topic.
 *
 * @param topic The topic title.
 * @param expanded Whether the row should be shown expanded with the topic body.
 * @param onClick Called when the row is clicked.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TopicRow(topic: String, expanded: Boolean, onClick: () -> Unit) {
    TopicRowSpacer(visible = expanded)
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = 2.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize()
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = topic,
                    style = MaterialTheme.typography.body1
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "lorem ipsum",
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
    TopicRowSpacer(visible = expanded)
}

/**
 * Shows a separator for topics.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TopicRowSpacer(visible: Boolean) {
    AnimatedVisibility(visible = visible) {
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun HomeFloatingActionButton(
    onClick: () -> Unit,
) {
    FloatingActionButton(onClick = onClick, modifier = Modifier.onGloballyPositioned {
        addPosition(it.boundsInRoot(), 2)
    }) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null
            )
        }
    }
}