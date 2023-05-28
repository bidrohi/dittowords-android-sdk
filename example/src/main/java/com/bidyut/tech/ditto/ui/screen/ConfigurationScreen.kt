package com.bidyut.tech.ditto.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.bidyut.tech.ditto.di.AppGraph
import com.bidyut.tech.ditto.example.Route

@Composable
fun ConfigurationScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    var tokenValue by remember {
        mutableStateOf("")
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        OutlinedTextField(
            value = tokenValue,
            onValueChange = {
                tokenValue = it
            },
            label = {
                Text("Ditto API Token")
            },
            modifier = Modifier.fillMaxWidth(),
        )
        Button(
            onClick = {
                AppGraph.instance.tokenStorage.key = tokenValue
                navController.popBackStack()
                Route.ProjectList.navigate(navController)
            },
            enabled = tokenValue.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Continue")
        }
    }
}
