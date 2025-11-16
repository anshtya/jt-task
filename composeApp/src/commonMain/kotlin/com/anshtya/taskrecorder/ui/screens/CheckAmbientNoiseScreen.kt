package com.anshtya.taskrecorder.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.anshtya.taskrecorder.platform.ui.MeasureAmbientNoiseView
import com.anshtya.taskrecorder.ui.components.BackButton
import com.anshtya.taskrecorder.ui.theme.MainTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import taskrecorder.composeapp.generated.resources.Res
import taskrecorder.composeapp.generated.resources.check_ambient_noise
import taskrecorder.composeapp.generated.resources.check_ambient_noise_check_again
import taskrecorder.composeapp.generated.resources.check_ambient_noise_description
import taskrecorder.composeapp.generated.resources.check_ambient_noise_heading
import taskrecorder.composeapp.generated.resources.check_ambient_noise_start_test
import taskrecorder.composeapp.generated.resources.sample_task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckAmbientNoiseScreen(
    onStartTestClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var checkNoise by rememberSaveable { mutableStateOf(false) }
    var startTest by rememberSaveable { mutableStateOf(false) }
    var message by rememberSaveable { mutableStateOf<String?>(null) }

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
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = stringResource(Res.string.check_ambient_noise_heading),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    text = stringResource(Res.string.check_ambient_noise_description),
                )
                MeasureAmbientNoiseView(
                    checkNoise = checkNoise,
                    onMeasured = { db ->
                        // difficult to get under 40 due to noisy conditions
                        message = if (db in 1..<60) {
                            startTest = true
                            "Average dB: $db. Good to proceed"
                        } else {
                            "Average dB: $db. Please move to quieter place"
                        }
                        checkNoise = false
                    },
                    modifier = Modifier
                        .size(300.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Button(
                    enabled = !checkNoise,
                    onClick = {
                        if (startTest) {
                            onStartTestClick()
                        } else {
                            message = null
                            checkNoise = true
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = if (startTest) {
                            stringResource(Res.string.check_ambient_noise_start_test)
                        } else {
                            stringResource(Res.string.check_ambient_noise)
                        },
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                message?.let {
                    if (startTest) {
                        OutlinedButton(
                            onClick = {
                                message = null
                                startTest = false
                                checkNoise = true
                            },
                            border = BorderStroke(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text(
                                text = stringResource(Res.string.check_ambient_noise_check_again),
                                fontWeight = FontWeight.SemiBold,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
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
            onStartTestClick = {},
            onBackClick = {}
        )
    }
}