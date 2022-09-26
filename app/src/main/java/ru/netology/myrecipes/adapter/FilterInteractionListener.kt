package ru.netology.myrecipes.adapter

interface FilterInteractionListener {

    fun checkboxFilterPressedOn(category: String)
    fun checkboxFilterPressedOff(category: String)
    fun getStatusCheckBox(category: String): Boolean
}