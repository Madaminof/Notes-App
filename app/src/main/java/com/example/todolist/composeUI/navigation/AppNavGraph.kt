package com.example.todolist.composeUI.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todolist.composeUI.screen.About.AboutScreen
import com.example.todolist.composeUI.screen.Calendar.CalendarScreen
import com.example.todolist.composeUI.screen.Details.TaskDetailScreen
import com.example.todolist.composeUI.screen.Exit.LogoutScreen
import com.example.todolist.composeUI.screen.Reminders.RemindersScreen
import com.example.todolist.composeUI.screen.addtask.AddTaskScreen
import com.example.todolist.composeUI.screen.home.HomeScreen
import com.example.todolist.composeUI.screen.home.HomeViewModel
import com.example.todolist.composeUI.screen.home.components.DrawerMenu
import com.example.todolist.composeUI.screen.settings.SettingsScreen
import com.example.todolist.composeUI.screen.settings.UserProfil.ProfileEditScreen
import kotlinx.coroutines.launch

object Routes {
    const val HOME = "home"
    const val ADD_TASK = "add_task"
    const val CALENDAR = "calendar"
    const val REMINDERS = "reminders"
    const val SETTINGS = "settings"
    const val ABOUT = "about"
    const val LOGOUT = "logout"
    const val EDIT_PROFILE = "profile_edit"

}
@Composable
fun AppNavGraph(navController: NavHostController) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToAddTask = { navController.navigate(Routes.ADD_TASK) },
                navController = navController
            )
        }

        composable(Routes.ADD_TASK) {
            AddTaskScreen(
                onTaskAdded = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SETTINGS) {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()

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
                SettingsScreen(
                    navController = navController, // 🔧 Qo‘shildi
                    onMenuClick = { scope.launch { drawerState.open() } }
                )
            }
        }

        composable(Routes.ABOUT) {
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
                AboutScreen(
                    navController = navController,
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    }
                )
            }
        }


        composable(Routes.LOGOUT) {
            LogoutScreen(navController = navController)
        }

        composable(Routes.CALENDAR) {
            CalendarScreen(viewModel = hiltViewModel(), navController = navController)
        }



        composable(Routes.REMINDERS) {
            RemindersScreen(navController = navController)
        }

        composable(Routes.EDIT_PROFILE) {
            ProfileEditScreen(
                navController = navController,
                onBack = { navController.popBackStack() }
            )
        }



        composable(
            route = "task_detail/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: return@composable
            val viewModel: HomeViewModel = hiltViewModel()
            val task = viewModel.tasks.collectAsState().value.find { it.id == taskId }

            if (task != null) {
                TaskDetailScreen(navController = navController, task = task, viewModel = viewModel)
            } else {
                Text("Task topilmadi")
            }
        }

        // Qo‘shimcha route'lar shu yerga qo‘shilishi mumkin
    }
}
