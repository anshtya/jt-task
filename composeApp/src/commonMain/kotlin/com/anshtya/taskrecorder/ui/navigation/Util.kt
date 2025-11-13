package com.anshtya.taskrecorder.ui.navigation

import androidx.navigation.NavController

fun NavController.navigateOnSaveTask() {
    navigate(AppDestination.TaskList) {
        popUpTo(AppDestination.NewTask)
    }
}