package com.gallapillo.jobarecruit.common

object Constants {
    // const for firebase
    const val USERS_COLLECTION = "Users"

    // const for registration
    val GENDERS_LIST = listOf<String>("Мужской", "Женский", "Другой")

    enum class SearchStatus {
        IN_ACTIVE_SEARCH, OPEN_FOR_OFFERS, DO_NOT_FOUND_JOB
    }
}