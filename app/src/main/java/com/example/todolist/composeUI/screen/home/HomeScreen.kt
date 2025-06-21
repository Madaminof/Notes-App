package com.example.todolist.composeUI.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todolist.composeUI.screen.EditScreen.TaskEditDialog
import com.example.todolist.composeUI.screen.home.components.DrawerMenu
import com.example.todolist.data.Task
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Notifications

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToAddTask: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
    val taskList by viewModel.tasks.collectAsState()
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
                onItemClick = {
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                LargeTopAppBarWithSearch(
                    searchQuery = searchQuery,
                    onSearchChange = { searchQuery = it },
                    scrollBehavior = scrollBehavior,
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onNavigateToAddTask,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 12.dp
                    ),
                    containerColor = Color(0xFF5886B4), // chiroyli ko‘k
                    contentColor = Color.White,
                    modifier = Modifier.shadow(8.dp, CircleShape) // qo‘shimcha soyadorlik
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            ,
            containerColor = Color(0xFFF7F7F7)
        ) { paddingValues ->
            val filteredList = taskList.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                        it.description.contains(searchQuery, ignoreCase = true)
            }

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalItemSpacing = 16.dp,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color(0xFFF7F7F7))
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
            ) {
                items(filteredList) { task ->
                    TaskCard(
                        task = task,
                        onClick = { navController.navigate("task_detail/${task.id}") },
                        onDelete = { viewModel.deleteTask(task) },
                        onEdit = { taskToEdit = it }
                    )
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeTopAppBarWithSearch(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    onMenuClick: () -> Unit
) {
    // 📱 System UI Controller: status va navigation bar ranglarini boshqarish
    val systemUiController = rememberSystemUiController()
    val statusBarColor = Color(0xFF5886B4)     // Telegram-style blue
    val navigationBarColor = Color.White       // Pastki navigation bar oq bo‘lsin
    val useDarkIcons = false                   // Status bar uchun oq ikonka

    // 🔄 Ranglarni bir marta o‘rnatish uchun
    LaunchedEffect(Unit) {
        // Faqat status bar rangini ko‘k qilamiz
        systemUiController.setStatusBarColor(
            color = statusBarColor,
            darkIcons = useDarkIcons
        )
        // Navigation bar rangini alohida oq qilamiz
        systemUiController.setNavigationBarColor(
            color = navigationBarColor,
            darkIcons = true
        )
    }

    // 🧭 AppBar va qidiruv maydoni
    Column {
        TopAppBar(
            title = {
                Text(
                    text = "Notes",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            },
            navigationIcon = {
                IconButton(onClick = onMenuClick) {
                    Icon(Icons.Default.Dehaze, contentDescription = "Menu", tint = Color.White)
                }
            },
            actions = {
                IconButton(onClick = {
                    // 🔔 Eslatma: bu yerda bildirishnoma funksiyasi qo‘shiladi
                }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = statusBarColor // Header fonini ko‘k qilish
            ),
            scrollBehavior = scrollBehavior
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search your notes", color = Color.Gray) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.Gray)
            },
            shape = RoundedCornerShape(24.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = statusBarColor,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
