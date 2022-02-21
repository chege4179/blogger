package com.peterchege.blogger.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.peterchege.blogger.room.entities.DraftRecord
import com.peterchege.blogger.util.Screens


@Composable
fun DraftCard(
    navController: NavController,
    draftRecord: DraftRecord,
    onDeleteDraft:(Int) -> Unit
){
    Card(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(60.dp)
            .border(BorderStroke(1.dp, Color.DarkGray))
            .clickable {
                navController.navigate(Screens.ADD_NEW_POST_SCREEN+
                        "?postTitle=${draftRecord.postTitle}&postBody=${draftRecord.postBody}")
            }
        ,

    ){
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 3.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(0.75f)
                    .padding(horizontal = 5.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = draftRecord.postTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    text = draftRecord.postBody,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Button(
                    onClick = {
                        draftRecord.id?.let { onDeleteDraft(it) }

                    }) {
                    Text(text = "Delete ")

                }
            }

        }
    }

}