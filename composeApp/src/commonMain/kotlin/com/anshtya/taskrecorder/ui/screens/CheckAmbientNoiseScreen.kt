package com.anshtya.taskrecorder.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.anshtya.taskrecorder.ui.components.BackButton
import com.anshtya.taskrecorder.ui.theme.MainTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import taskrecorder.composeapp.generated.resources.Res
import taskrecorder.composeapp.generated.resources.check_ambient_noise_description
import taskrecorder.composeapp.generated.resources.check_ambient_noise_heading
import taskrecorder.composeapp.generated.resources.check_ambient_noise_start_test
import taskrecorder.composeapp.generated.resources.sample_task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckAmbientNoiseScreen(
    onStartClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var startTest by rememberSaveable { mutableStateOf(true) }  // TODO: Disable according to input

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(Res.string.sample_task))
                },
                navigationIcon = {
                    BackButton { onBackClick() }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = stringResource(Res.string.check_ambient_noise_heading),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Spacer(Modifier.height(10.dp))
                Text(
                    text = stringResource(Res.string.check_ambient_noise_description),
                )
                Spacer(Modifier.height(10.dp))
                Button(
                    onClick = onStartClick,
                    shape = RoundedCornerShape(10.dp),
                    enabled = startTest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.check_ambient_noise_start_test),
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CheckAmbientNoiseScreenPreview() {
    MainTheme {
        CheckAmbientNoiseScreen(
            onStartClick = {},
            onBackClick = {}
        )
    }
}