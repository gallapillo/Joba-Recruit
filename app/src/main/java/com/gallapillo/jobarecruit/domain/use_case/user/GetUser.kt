package com.gallapillo.jobarecruit.domain.use_case.user

import com.gallapillo.jobarecruit.domain.repository.UserRepository
import javax.inject.Inject

class GetUser @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(userId: String) = repository.getUserById(userId)
}