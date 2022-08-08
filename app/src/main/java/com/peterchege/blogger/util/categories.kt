package com.peterchege.blogger.util


val categories = listOf<categoryItem>(
    categoryItem(1,"Entertainment"),
    categoryItem(2,"Politics"),
    categoryItem(3,"Sports"),
    categoryItem(4,"Monkey-Pox"),
    categoryItem(4,"COVID-19"),
    categoryItem(4,"Music"),
    categoryItem(4,"Lifestyle"),


    )

data class categoryItem(
    val id: Int,
    val name: String
)