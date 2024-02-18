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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.core.room.entities.DraftPost
import com.peterchege.blogger.data.DraftRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

sealed interface DraftsScreenUiState {

    object Loading : DraftsScreenUiState

    data class Success(val drafts: List<DraftPost>) : DraftsScreenUiState

    data class Error(val message: String) : DraftsScreenUiState


}

data class DeleteDraftState(
    val selectedDraftToBeDeleted: DraftPost? = null
)


@HiltViewModel
class DraftScreenViewModel @Inject constructor(
    private val draftRepository: DraftRepositoryImpl
) : ViewModel() {


    val uiState = draftRepository.getAllDrafts()
        .map { DraftsScreenUiState.Success(it) }
        .onStart { DraftsScreenUiState.Loading }
        .catch { DraftsScreenUiState.Error(message = "An unexpected error occurred while fetching your drafts") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = DraftsScreenUiState.Loading,
        )

    private val _deleteDraftUiState = MutableStateFlow(DeleteDraftState())
    val deleteDraftUiState = _deleteDraftUiState.asStateFlow()

    fun deleteDraft(id: Int) {
        viewModelScope.launch {
            draftRepository.deleteDraftById(id)
            toggleDeleteDraftVisibility(draftPost = null)
        }
    }


    fun toggleDeleteDraftVisibility(draftPost: DraftPost?){
        _deleteDraftUiState.update {
            it.copy(
                selectedDraftToBeDeleted = draftPost,
            )
        }
    }

}