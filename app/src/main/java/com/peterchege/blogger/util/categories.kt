package com.peterchege.blogger.util


val categories = listOf<categoryItem>(
    categoryItem(1,"Top-stories"),
    categoryItem(1,"Entertainment"),
    categoryItem(2,"Politics"),
    categoryItem(3,"Sports"),
    categoryItem(4,"Monkey-Pox"),
    categoryItem(5,"COVID-19"),
    categoryItem(6,"Music"),
    categoryItem(7,"Lifestyle"),

    )

data class categoryItem(
    val id: Int,
    val name: String
)