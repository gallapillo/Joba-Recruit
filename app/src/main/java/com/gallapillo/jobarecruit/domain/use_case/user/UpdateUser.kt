package com.gallapillo.jobarecruit.domain.use_case.user

import com.gallapillo.jobarecruit.domain.model.User
import com.gallapillo.jobarecruit.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUser @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(user: User) = repository.saveUserChanges(user)
}