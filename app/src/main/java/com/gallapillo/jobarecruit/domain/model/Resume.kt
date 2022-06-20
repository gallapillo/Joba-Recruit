package com.gallapillo.jobarecruit.domain.model

data class Resume (
    var id: String,
    var name: String,
    var description: String,
    val experience: String,
)