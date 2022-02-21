package com.peterchege.blogger.ui.dashboard.draft_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.room.entities.DraftRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class DraftScreenViewModel @Inject constructor(
    private val draftRepository: DraftRepository
):ViewModel() {
    private val _drafts = mutableStateOf<List<DraftRecord>>(emptyList())
    val drafts :State<List<DraftRecord>> = _drafts
    init {
        getDrafts()
    }


    private fun getDrafts(){
        viewModelScope.launch {
            try {
                val draftslist = draftRepository.getAllDrafts()
                _drafts.value = draftslist
            }catch (e:IOException){

            }
        }
    }
    fun deleteDraft(id:Int){
        viewModelScope.launch {
            try {
                draftRepository.deleteDraftById(id)
                getDrafts()
            }catch (e:IOException){

            }
        }

    }

}