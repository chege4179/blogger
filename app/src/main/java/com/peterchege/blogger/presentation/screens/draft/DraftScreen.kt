/*
 * Copyright 2023 Blogger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.peterchege.blogger.presentation.screens.draft

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peterchege.blogger.R
import com.peterchege.blogger.core.room.entities.DraftPost
import com.peterchege.blogger.presentation.alertDialogs.DeleteDraftDialog
import com.peterchege.blogger.presentation.components.CustomIconButton
import com.peterchege.blogger.presentation.components.DraftCard
import com.peterchege.blogger.presentation.components.ErrorComponent
import com.peterchege.blogger.presentation.components.LoadingComponent
import com.peterchege.blogger.presentation.theme.defaultPadding


@Composable
fun DraftScreen(
    navigateToAddPostScreen: (Int) -> Unit,
    navigateBack: () -> Unit,
    viewModel: DraftScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val deleteDraftState by viewModel.deleteDraftUiState.collectAsStateWithLifecycle()
    DraftScreenContent(
        navigateToAddPostScreen = navigateToAddPostScreen,
        uiState = uiState,
        deleteDraftState = deleteDraftState,
        deleteDraft = viewModel::deleteDraft,
        toggleDeleteDraftDialog = viewModel::toggleDeleteDraftVisibility,
        navigateBack = navigateBack
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DraftScreenContent(
    navigateToAddPostScreen: (Int) -> Unit,
    deleteDraft: (Int) -> Unit,
    deleteDraftState: DeleteDraftState,
    uiState: DraftsScreenUiState,
    toggleDeleteDraftDialog: (DraftPost?) -> Unit,
    navigateBack:() -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    CustomIconButton(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.go_back),
                        onClick = navigateBack
                    )
                },
                title = {
                    Text(text = stringResource(id = R.string.draft_screen_header))
                }
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is DraftsScreenUiState.Loading -> {
                LoadingComponent()
            }

            is DraftsScreenUiState.Error -> {
                ErrorComponent(
                    retryCallback = { /*TODO*/ },
                    errorMessage = uiState.message
                )
            }

            is DraftsScreenUiState.Success -> {
                if (deleteDraftState.selectedDraftToBeDeleted != null) {
                    DeleteDraftDialog(
                        draftPost = deleteDraftState.selectedDraftToBeDeleted,
                        onDeleteDraftConfirm = {
                            deleteDraftState.selectedDraftToBeDeleted.id?.let {
                                deleteDraft(it)
                            }
                        },
                        onDismiss = { toggleDeleteDraftDialog(null) }
                    )
                }
                val drafts = uiState.drafts
                if (drafts.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = stringResource(id = R.string.empty_draft_message),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues = paddingValues)
                            .padding(defaultPadding)
                    ) {
                        items(items = drafts) { draft ->
                            DraftCard(
                                draftRecord = draft,
                                navigateToAddPostScreen = navigateToAddPostScreen,
                                onDeleteDraft = {
                                    toggleDeleteDraftDialog(draft)
                                }
                            )
                        }
                    }
                }
            }
        }

    }
}