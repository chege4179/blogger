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
package com.peterchege.blogger.util


val categories = listOf<categoryItem>(
    categoryItem(1,"General"),
    categoryItem(2,"Top-stories"),
    categoryItem(3,"Entertainment"),
    categoryItem(4,"Politics"),
    categoryItem(5,"Sports"),
    categoryItem(6,"Monkey-Pox"),
    categoryItem(7,"COVID-19"),
    categoryItem(8,"Music"),
    categoryItem(9,"Lifestyle"),
    categoryItem(10,"Business"),
    categoryItem(11,"Science"),
    categoryItem(12,"Health"),

    )
data class categoryItem(
    val id: Int,
    val name: String
)