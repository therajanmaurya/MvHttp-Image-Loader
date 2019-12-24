package com.github.mvhttpclient.models

import androidx.room.*
import com.github.mvhttpclient.db.ProfileImageConverter
import com.google.gson.annotations.SerializedName

@Entity
data class ImageData(

    @PrimaryKey
    val id: String,
    val width: Int,
    val height: Int,
    val color: String,
    val likes: Int,
    val urls: Urls,
    val links: Links,
    @Embedded(prefix = "user_") val user: User,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("liked_by_user")
    val likedByUser: Boolean

)


data class User(
    val id: String,
    val username: String,
    val name: String,
    val links: Links,

    @field:SerializedName("profile_image")
    val profileImage: ProfileImage
)

data class ProfileImage(
    val small: String,
    val medium: String,
    val large: String
)

data class Links(
    val self: String,
    val html: String,
    val photos: String,
    val likes: String
)

data class Urls(
    val raw: String,
    val full: String,
    val regular: String,
    val small: String
)
