package com.example.bookshelf.model

data class MUser(val userId: String, val avatarUrl: String, val displayName: String) {
    fun toMap(): MutableMap<String, String> {
        return mutableMapOf(
            "User_Id" to this.userId,
            "avatarUrl" to this.avatarUrl,
            "displayName" to this.displayName
        )
    }
}