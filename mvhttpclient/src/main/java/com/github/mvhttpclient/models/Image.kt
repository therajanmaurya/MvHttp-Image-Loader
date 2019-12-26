package com.github.mvhttpclient.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Image(
    @PrimaryKey
    val imageUrl: String,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val data: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image

        if (imageUrl != other.imageUrl) return false
        if (data != null) {
            if (other.data == null) return false
            if (!data.contentEquals(other.data)) return false
        } else if (other.data != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = imageUrl.hashCode()
        result = 31 * result + (data?.contentHashCode() ?: 0)
        return result
    }
}