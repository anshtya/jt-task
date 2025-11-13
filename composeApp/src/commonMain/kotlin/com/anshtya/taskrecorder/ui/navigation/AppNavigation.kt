package com.anshtya.taskrecorder.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anshtya.taskrecorder.ui.screens.CheckAmbientNoiseScreen
import com.anshtya.taskrecorder.ui.screens.NewTaskScreen
import com.anshtya.taskrecorder.ui.screens.TaskSelectionScreen
import com.anshtya.taskrecorder.ui.screens.recordtask.RecordTaskRoute
import com.anshtya.taskrecorder.ui.screens.tasklist.TaskListRoute

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.NewTask
    ) {
        composable<AppDestination.NewTask> {
            NewTaskScreen(
                onStartTaskClick = {
                    navController.navigate(AppDestination.CheckAmbientNoise)
                }
            )
        }
        composable<AppDestination.CheckAmbientNoise> {
            CheckAmbientNoiseScreen(
                onStartClick = {
                    navController.navigate(AppDestination.TaskSelection)
                },
                onBackClick = navController::navigateUp
            )
        }
        composable<AppDestination.TaskSelection> {
            TaskSelectionScreen(
                onTaskClick = {
                    navController.navigate(AppDestination.RecordTaskScreen(it))
                },
                onBackClick = navController::navigateUp
            )
        }
        composable<AppDestination.RecordTaskScreen> {
            RecordTaskRoute(
                onNavigateUp = navController::navigateUp
            )
        }
        composable<AppDestination.TaskList> {
            TaskListRoute(
                onNavigateUp = navController::navigateUp
            )
        }
    }
}