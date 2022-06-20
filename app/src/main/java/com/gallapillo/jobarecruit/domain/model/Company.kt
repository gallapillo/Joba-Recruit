package com.gallapillo.jobarecruit.domain.model

data class Company(
    var name: String = "",
    var description: String = "",
    var imageUrl: String = "",
    var raiting: Int = 0,
    var feedbacks: List<FeedBack> = emptyList(),
    var ownerUser: User? = null,
)
