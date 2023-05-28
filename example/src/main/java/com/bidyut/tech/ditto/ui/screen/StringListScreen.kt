package com.bidyut.tech.ditto.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bidyut.tech.ditto.service.json.ProjectId
import kotlinx.coroutines.launch

@Composable
fun StringListScreen(
    projectId: ProjectId,
    modifier: Modifier = Modifier,
    viewModel: StringListViewModel = viewModel(),
) {
    val scope = rememberCoroutineScope()
    val snackbarHost = remember { SnackbarHostState() }
    val clipboardManager = LocalClipboardManager.current
    LaunchedEffect(true) {
        scope.launch {
            viewModel.effect.collect {
                when (it) {
                    is StringListContract.Effect.CopyKeyToClipboard -> {
                        clipboardManager.setText(AnnotatedString(it.key))
                        snackbarHost.showSnackbar(
                            message = "String key copied!"
                        )
                    }
                }
            }
        }
    }
    val state = viewModel.viewState.collectAsState().value
    when (state) {
        is StringListContract.UiState.Connecting -> {
            CircularProgressIndicator()
            viewModel.sendTrigger(
                StringListContract.Trigger.FetchStringList(
                    projectId,
                    state.variantId
                )
            )
        }

        is StringListContract.UiState.Error -> {
            Text(text = state.message)
        }

        is StringListContract.UiState.Strings -> {
            Column(
                modifier = modifier,
            ) {
                if (state.variants.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        items(state.variants) {
                            val onClick = {
                                viewModel.sendTrigger(
                                    StringListContract.Trigger.VariantChanged(
                                        projectId,
                                        it.apiId
                                    )
                                )
                            }
                            if (it.apiId == state.variantId) {
                                FilledTonalButton(
                                    onClick = onClick,
                                    content = { Text(it.displayName) },
                                )
                            } else {
                                OutlinedButton(
                                    onClick = onClick,
                                    content = { Text(it.displayName) },
                                )
                            }
                            Divider(
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
                StringsListView(
                    strings = state.strings,
                    onItemClick = {
                        viewModel.sendTrigger(StringListContract.Trigger.StringClicked(it))
                    },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
fun StringsListView(
    strings: Map<String, String>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(4.dp),
    ) {
        items(strings.entries.toList()) {
            StringRow(key = it.key,
                value = it.value,
                modifier = Modifier.clickable {
                    onItemClick(it.key)
                })
            Divider()
        }
    }
}

@Composable
fun StringRow(
    key: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineContent = { Text(key) },
        supportingContent = { Text(value) },
        modifier = modifier.padding(4.dp),
    )
}
