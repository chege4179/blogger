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