package com.example.todolist.composeUI.screen.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dehaze
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todolist.composeUI.screen.EditScreen.TaskEditDialog
import com.example.todolist.composeUI.screen.home.components.DrawerMenu
import com.example.todolist.data.Task
import com.example.todolist.ui.theme.BrandColor
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToAddTask: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
    val systemUiController = rememberSystemUiController()

    val taskList by viewModel.tasks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var taskToEdit by remember { mutableStateOf<Task?>(null) }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenu(
                navController = navController,
                onItemClick = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                LargeTopAppBarWithSearch(
                    searchQuery = searchQuery,
                    onSearchChange = { searchQuery = it },
                    scrollBehavior = scrollBehavior,
                    navController = navController,
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onNavigateToAddTask,
                    shape = CircleShape,
                    containerColor = BrandColor,
                    contentColor = colorScheme.onPrimary,
                    modifier = Modifier.shadow(8.dp, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            containerColor = colorScheme.background
        ) { paddingValues ->

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center

                ) {
                    CircularProgressIndicator(color = BrandColor)
                }
            } else {
                val filteredList = taskList.filter {
                    it.title.contains(searchQuery, ignoreCase = true) ||
                            it.description.contains(searchQuery, ignoreCase = true)
                }

                val animatedItems = remember { mutableStateListOf<Int>() }

                LaunchedEffect(filteredList) {
                    animatedItems.clear()
                    filteredList.forEachIndexed { index, task ->
                        delay(index * 80L) // har bir itemni chiqish oralig‘i 100ms
                        animatedItems.add(task.id)
                    }
                }

                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    verticalItemSpacing = 16.dp,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .background(colorScheme.background)
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                ) {
                    items(filteredList, key = { it.id }) { task ->
                        val visible = animatedItems.contains(task.id)
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(animationSpec = tween(300)) +
                                    scaleIn(
                                        initialScale = 0.8f,
                                        animationSpec = tween(300)
                                    )

                        ) {
                            TaskCard(
                                task = task,
                                onClick = { navController.navigate("task_detail/${task.id}") },
                                onDelete = { viewModel.deleteTask(task) },
                                onEdit = { taskToEdit = it }
                            )
                        }
                    }
                }

                taskToEdit?.let { task ->
                    TaskEditDialog(
                        task = task,
                        onDismiss = { taskToEdit = null },
                        onConfirm = {
                            viewModel.updateTask(it)
                            taskToEdit = null
                        }
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(
            color = BrandColor,
            darkIcons = false // false: oq ikonlar, chunki fon to'q rang
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeTopAppBarWithSearch(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    onMenuClick: () -> Unit,
    navController: NavController
    ) {
    Column {
        TopAppBar(
            title = {
                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = colorScheme.onPrimary
                )
            },
            navigationIcon = {
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.Dehaze,
                        contentDescription = "Menu",
                        tint = colorScheme.onPrimary
                    )
                }
            },
            actions = {
                IconButton(onClick = { navController.navigate("reminders") }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = colorScheme.onPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = BrandColor,
                scrolledContainerColor = BrandColor
            ),
            scrollBehavior = scrollBehavior
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = {
                Text("Search your notes", color = colorScheme.onBackground.copy(alpha = 0.5f))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = colorScheme.onBackground.copy(alpha = 0.5f)
                )
            },
            shape = RoundedCornerShape(24.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = colorScheme.outline,
                focusedBorderColor = BrandColor,
                cursorColor = BrandColor,
                focusedTextColor = colorScheme.onBackground,
                unfocusedTextColor = colorScheme.onBackground,
                focusedContainerColor = colorScheme.background,
                unfocusedContainerColor = colorScheme.background
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
