package com.anshtya.taskrecorder.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anshtya.taskrecorder.platform.camera.CameraScreen
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
                onStartTestClick = {
                    navController.navigate(AppDestination.TaskSelection) {
                        popUpTo(AppDestination.CheckAmbientNoise) {
                            inclusive = true
                        }
                    }
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
        composable<AppDestination.RecordTaskScreen> { backStackEntry ->
            val capturePhotoPath = backStackEntry.savedStateHandle.get<String?>("photo")
            RecordTaskRoute(
                capturedPhotoPath = capturePhotoPath,
                onNavigateUp = navController::navigateUp,
                onNavigateToCamera = {
                    navController.navigate(AppDestination.Camera)
                },
                onNavigateToTaskHistory = {
                    navController.navigateOnSaveTask()
                }
            )
        }
        composable<AppDestination.Camera> {
            CameraScreen(
                onNavigateToTask = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("photo", it)
                    navController.popBackStack()
                }
            )
        }
        composable<AppDestination.TaskList> {
            TaskListRoute(
                onNavigateUp = navController::navigateUp
            )
        }
    }
}