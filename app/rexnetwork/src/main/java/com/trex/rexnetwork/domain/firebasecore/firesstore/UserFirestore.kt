package com.trex.rexnetwork.domain.firebasecore.firesstore

import com.trex.rexnetwork.data.BaseFirestoreResponse
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class User(
    val email: String = "",
    val password: String = "",
    val shopId: String = UUID.randomUUID().toString(),
) : BaseFirestoreResponse

class UserFirestore : FirestoreBase<User>("users") {
    override fun dataClass(): Class<User> = User::class.java

    fun getUserIfExists(
        email: String,
        onUserFound: (User) -> Unit,
        onUserNotFound: () -> Unit,
    ) {
        getDocument(email, dataClass(), {
            onUserFound(it)
        }, { error ->
            if (error.message == FireStoreExeptions.DOC_NOT_FOUND.toString()) {
                onUserNotFound()
            }
        })
    }

    fun createUser(
        user: User,
        onUserAlreadyExists: (User) -> Unit,
        onUserCreatedSuccessfully: (User) -> Unit,
        onUserCreationFailed: () -> Unit,
    ) {
        getUserIfExists(user.email, {
            onUserAlreadyExists(it)
        }, {
            addOrUpdateDocument(user.email, user, {
                onUserCreatedSuccessfully(user)
            }, {
                onUserCreationFailed()
            })
        })
    }
}
