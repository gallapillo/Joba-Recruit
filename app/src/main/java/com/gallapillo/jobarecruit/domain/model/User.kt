package com.gallapillo.jobarecruit.domain.model

import com.gallapillo.jobarecruit.common.Constants

data class User(
    var name: String = "",
    var surName: String = "",
    var userId: String = "",
    var email: String = "",
    var password: String = "",
    var userRole: String = "recruit",
    var birthDay: String = "",
    var gender: String = "Other",
    var resume: List<Resume> = emptyList(),
    var vacancy: List<Vacancy> = emptyList(),
    var company: Company = Company(),
    var wannaFindJob: Int = Constants.SearchStatus.IN_ACTIVE_SEARCH.ordinal
)