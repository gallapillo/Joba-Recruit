package com.gallapillo.jobarecruit.domain.repository

import com.gallapillo.jobarecruit.common.Response
import com.gallapillo.jobarecruit.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUserById(userId: String) : Flow<Response<User>>

    fun getAllUsers(userId: String) : Flow<List<User>>

    fun saveUserChanges(user: User) : Flow<Response<Boolean>>
}