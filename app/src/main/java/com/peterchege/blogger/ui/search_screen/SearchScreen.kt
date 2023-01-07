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
package com.peterchege.blogger.ui.search_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.pager.*
import com.peterchege.blogger.ui.search_screen.tabs.SearchPostsTab
import com.peterchege.blogger.ui.search_screen.tabs.SearchUsersTab
import com.peterchege.blogger.ui.theme.DarkBlue
import com.peterchege.blogger.ui.theme.MainWhiteColor
import com.peterchege.blogger.ui.theme.testColor
import com.peterchege.blogger.util.Constants
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: SearchProductScreenViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        topBar = {
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = CenterVertically
            ) {
                TextField(
                    value = viewModel.searchTerm.value,
                    onValueChange = {
                        viewModel.onChangeSearchTerm(it)
                    },
                    placeholder = {
                        Text(
                            text = "Search",
                        )
                    },

                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                        .clickable {
                        },
                    shape = RoundedCornerShape(size = 8.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        autoCorrect = true,
                        keyboardType = KeyboardType.Text,
                    ),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.White,
                        disabledTextColor = MainWhiteColor,
                        backgroundColor = MainWhiteColor,
                        focusedIndicatorColor = MainWhiteColor,
                        unfocusedIndicatorColor = MainWhiteColor,
                        disabledIndicatorColor = MainWhiteColor
                    ),
                    textStyle = TextStyle(color = Color.Black),
                    maxLines = 1,
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Product",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {

                                }
                        )
                    }
                )

//                IconButton(
//                    onClick = {
//
//                    }) {
//
//                    Icon(
//                        modifier = Modifier
//                            .size(55.dp)
//                            .clip(
//                                shape = RoundedCornerShape(
//                                    size = 8.dp
//                                )
//                            )
//                            .background(MainWhiteColor)
//                            .padding(start = 4.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
//                        imageVector = Icons.Default.FilterList,
//                        contentDescription = null,
//                        tint = DarkBlue
//                    )
//                }
            }
        },
    ) {
        val pagerState = rememberPagerState()
        Column(
            modifier = Modifier.background(Color.White)
        ) {
            Tabs(pagerState = pagerState)
            TabsContent(
                pagerState = pagerState,
                navController = navController

            )
        }
    }

}


@ExperimentalPagerApi
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf(Constants.SEARCH_TYPE_POSTS,Constants.SEARCH_TYPE_USERS)
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        backgroundColor = Color.White,
        contentColor = Color.White,
        divider = {
            TabRowDefaults.Divider(
                thickness = 2.dp,
                color = Color.White
            )
        },
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                height = 2.dp,
                color = testColor
            )
        }
    ) {
        list.forEachIndexed { index, _->
            Tab(
                text = {
                    Text(
                        list[index],
                        color = if (pagerState.currentPage == index) testColor else Color.LightGray
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@ExperimentalPagerApi
@Composable
fun TabsContent(pagerState: PagerState, navController: NavHostController) {
    HorizontalPager(state = pagerState ,count =2) { page ->
        when(page) {
            0 -> SearchPostsTab(navHostController = navController)
            1 -> SearchUsersTab(navHostController = navController)

        }
    }
}
