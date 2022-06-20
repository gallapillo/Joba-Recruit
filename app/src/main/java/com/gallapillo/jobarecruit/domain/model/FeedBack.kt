package com.gallapillo.jobarecruit.domain.model

data class FeedBack (
    var owner: User?,
    var raiting: Int = 0,
    var description: String
)