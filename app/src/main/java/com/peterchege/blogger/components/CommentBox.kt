package com.peterchege.blogger.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.peterchege.blogger.api.responses.Comment
import com.peterchege.blogger.util.Constants
import com.peterchege.blogger.util.Screens


@ExperimentalCoilApi
@Composable
fun CommentBox(
    comment: Comment,
    navController: NavController,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
        ,
    ){
        Row(
            modifier = Modifier.fillMaxWidth()
        ,

        ){
            Image(
                modifier = Modifier
                    .fillMaxWidth(0.15f)
                    .height(60.dp),
                painter = rememberImagePainter(
                    data = comment.imageUrl,
                    builder = {
                        crossfade(true)

                    },
                ),

                contentDescription = comment.comment)
            Column(
                Modifier.fillMaxHeight(0.1f)

            ) {
                Text(
                    fontWeight =FontWeight.Bold,
                    text = comment.username,
                    modifier = Modifier
                        .padding(bottom = 5.dp)
                        .clickable {
                            navController.navigate(Screens.AUTHOR_PROFILE_SCREEN + "/${comment.username}")
                        }
                )
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 60.dp)
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(5.dp)


                ){
                    Text(
                        text = comment.comment,
                        textAlign = TextAlign.Start,
                        textDecoration = TextDecoration.None,
                        color = Color.Black,
                    )

                }
            }


        }

    }

}