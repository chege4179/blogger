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
package com.peterchege.blogger.presentation.screens.dashboard.draft_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.peterchege.blogger.presentation.components.DraftCard

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DraftScreen(
    navController: NavController,
    viewModel: DraftScreenViewModel = hiltViewModel()
){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text= "My Drafts",
                    )
                }
                ,
                backgroundColor = MaterialTheme.colors.primary)
        }
    ) {
        if (viewModel.drafts.value.isEmpty()){
            Box(modifier = Modifier.fillMaxSize()){
                Text(
                    text = "You have no drafts",
                    Modifier.align(Alignment.Center)
                )
            }
        }else{
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ){
                items(viewModel.drafts.value){ draft ->
                    DraftCard(
                        draftRecord = draft,
                        navController = navController,
                        onDeleteDraft = {
                            viewModel.deleteDraft(it)
                        }
                    )
                }
            }
        }


    }

}